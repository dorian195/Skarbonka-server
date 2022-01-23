package pl.polsl.skarbonka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.model.Donation;
import pl.polsl.skarbonka.query.DonatorsQueryResult;
import pl.polsl.skarbonka.repository.DonationRepository;

import java.util.List;

@Service
public class DonationService {
    private DonationRepository donationRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public List<Donation> findAllByFundraisingId(Long fundraisingId, int page, int pageSize, boolean sort) {
        if (sort) {
            return donationRepository.findAllByFundraisingId(fundraisingId, PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createdDate")))).toList();
        } else {
            return donationRepository.findAllByFundraisingId(fundraisingId, PageRequest.of(page, pageSize, Sort.unsorted())).toList();
        }
    }

    public List<DonatorsQueryResult> findDistinctUsersByFundraisingId(Long fundraisingId, int page, int pageSize, boolean sort) {
        if (sort) {
            return donationRepository.findDistinctUsersByFundraisingId(fundraisingId, PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("ammount")))).toList();
        } else {
            return donationRepository.findDistinctUsersByFundraisingId(fundraisingId, PageRequest.of(page, pageSize, Sort.unsorted())).toList();
        }
    }

    public Donation save(Donation donation) {
        return donationRepository.save(donation);
    }

    public Donation findDonationById(Long id) {
       return donationRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
