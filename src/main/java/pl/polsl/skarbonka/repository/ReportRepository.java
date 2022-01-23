package pl.polsl.skarbonka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.polsl.skarbonka.model.Report;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    void deleteAllByFundraisingId(Long id);

    @Query("select r from Report r where r.fundraising.id=:id")
    List<Report> findAllByFundraisingId(Long id);

    @Query("select r from Report r where r.fundraising.id=:id and r.user.id=:userId")
    Optional<Report> findByIdAndUserId(Long id, Long userId);

}
