package pl.polsl.skarbonka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.polsl.skarbonka.model.Donation;
import pl.polsl.skarbonka.query.DonatorsQueryResult;

public interface DonationRepository extends PagingAndSortingRepository<Donation, Long> {

    @Query("Select d from Donation d where d.fundraising.id=:fundraisingId")
    Page<Donation> findAllByFundraisingId(Long fundraisingId, Pageable pageable);

    @Query("select new pl.polsl.skarbonka.query.DonatorsQueryResult(d.user, d.ammount) " +
            "from Donation as d " +
            "where d.fundraising.id=:fundraisingId")
    Page<DonatorsQueryResult> findDistinctUsersByFundraisingId(Long fundraisingId, Pageable pageable);
}
