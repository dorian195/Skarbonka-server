package pl.polsl.skarbonka.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.polsl.skarbonka.model.Fundraising;
import pl.polsl.skarbonka.model.SortField;
import pl.polsl.skarbonka.repository.FundraisingRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FundraisingService {
    private FundraisingRepository fundraisingRepository;

    @Autowired
    public FundraisingService(FundraisingRepository fundraisingRepository) {
        this.fundraisingRepository = fundraisingRepository;
    }

    public Optional<Fundraising> findById(Long id) {
        return fundraisingRepository.findById(id);
    }

    public List<Fundraising> findByNameAndCategory(String name, String category, int page,
                                                   int pageSize, boolean sort, SortField sf) {
        String sortField = sf.name().equals("POPULAR") ? "accountBalance" : "createdDate";
        Sort.Order order = Sort.Order.desc(sortField);
        if ("all".equalsIgnoreCase(category)) {
            category = "";
        }
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(category)) {
            return fundraisingRepository.findAll(PageRequest.of(page, pageSize, sort ? Sort.by(order) : Sort.unsorted())).toList();
        } else if (StringUtils.isEmpty(name) && !StringUtils.isEmpty(category)) {
            return fundraisingRepository.findByCategory(category, PageRequest.of(page, pageSize, sort == true ? Sort.by(order) : Sort.unsorted())).toList();
        } else if (!StringUtils.isEmpty(name) && StringUtils.isEmpty(category)) {
            return fundraisingRepository.findByName(name, PageRequest.of(page, pageSize, sort == true ? Sort.by(order) : Sort.unsorted())).toList();
        }
        return fundraisingRepository.findByNameAndCategory(name, category, PageRequest.of(page, pageSize, sort == true ? Sort.by(order) : Sort.unsorted())).toList();
    }

    public List<Fundraising> findAll(int page, int pageSize, boolean sort) {
        if (sort) {
            return fundraisingRepository.findAllByDeleteDateIsNotNull(PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("name")))).toList();
        } else {
            return fundraisingRepository.findAllByDeleteDateIsNotNull(PageRequest.of(page, pageSize, Sort.unsorted())).toList();
        }
    }

    public Fundraising save(Fundraising fundraising) {
        return fundraisingRepository.save(fundraising);
    }

    public Fundraising modify(Fundraising fundraising) {
        Optional<Fundraising> found = findById(fundraising.getId());
        if (found.isEmpty()) {
            return null;
        }
        Fundraising oldFundraising = found.get();
        modifyEntity(oldFundraising, fundraising);
        return fundraisingRepository.save(oldFundraising);
    }

    private void modifyEntity(Fundraising oldFundraising, Fundraising fundraising) {
        oldFundraising.setModificationDate(Date.from(Instant.now()));
        oldFundraising.setCategory(fundraising.getCategory());
        oldFundraising.setDescription(fundraising.getDescription());
        oldFundraising.setName(fundraising.getName());
        oldFundraising.setEndDate(fundraising.getEndDate());
    }

    public boolean deleteById(Long id) {
        Fundraising fundraising = fundraisingRepository.findById(id).get();
        fundraising.setDeleteDate(Date.from(Instant.now()));
        fundraisingRepository.save(fundraising);
        return true;
    }

    public Optional<Fundraising> reportById(Long id) {
        Optional<Fundraising> byId = fundraisingRepository.findById(id);
        if (byId.isEmpty()) {
            return Optional.empty();
        }
        Fundraising fundraising = byId.get();
        fundraising.addReport();
        return Optional.of(fundraisingRepository.save(fundraising));
    }

    @Scheduled(fixedDelay = 86400000)
    public void scheduleFixedDelayTask() {
        List<Fundraising> allExpired = findAllExpired();
        allExpired.forEach(fund -> deleteById(fund.getId()));
    }

    private List<Fundraising> findAllExpired() {
        return fundraisingRepository.findAllExpiredByDeleteDateIsNotNull();
    }

    public List<Fundraising> findByUserId(Long userId, int page, int pageSize, boolean sort) {
        return fundraisingRepository.findByUserId(userId, PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("name")))).toList();
    }
}
