package pl.polsl.skarbonka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.skarbonka.model.Fundraising;
import pl.polsl.skarbonka.model.Report;
import pl.polsl.skarbonka.model.User;
import pl.polsl.skarbonka.repository.ReportRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository ReportRepository) {
        this.reportRepository = ReportRepository;
    }

    public void addToReports(Fundraising fundraising, User user) {
        Report Report = new Report(user, fundraising);
        reportRepository.save(Report);
    }

    public void deleteAllByFundraisingId(Long id) {
        List<Report> allByFundraisingId = reportRepository.findAllByFundraisingId(id);
        allByFundraisingId.forEach(fav -> deleteById(fav.getId()));
    }

    public boolean deleteById(Long id) {
        reportRepository.deleteById(id);
        Optional<Report> favorite = reportRepository.findById(id);
        return favorite.isEmpty();
    }

    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }

    public Optional<Report> findByFundraisingIdAndUserId(Long id, Long userId) {
        return reportRepository.findByIdAndUserId(id, userId);
    }
}
