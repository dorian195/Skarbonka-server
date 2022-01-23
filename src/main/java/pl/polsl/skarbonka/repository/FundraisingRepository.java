package pl.polsl.skarbonka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.polsl.skarbonka.model.Fundraising;

import java.util.List;

public interface FundraisingRepository extends PagingAndSortingRepository<Fundraising, Long> {
    @Query("Select f from Fundraising f join Category c on c.id=f.category.id where lower(f.name) like lower(concat('%', :name,'%')) and lower(c.name) = lower(:category) and f.deleteDate is null")
    Page<Fundraising> findByNameAndCategory(String name, String category, Pageable pageable);

    @Query("Select f from Fundraising f join Category c on c.id=f.category.id where lower(f.category.name) = lower(:category) and f.deleteDate is null")
    Page<Fundraising> findByCategory(String category, Pageable pageable);

    @Query("Select f from Fundraising f where lower(f.name) like lower(concat('%', :name,'%')) and f.deleteDate is null")
    Page<Fundraising> findByName(String name, Pageable pageable);

    @Query("Select f from Fundraising f where f.deleteDate is null")
    Page<Fundraising> findAllByDeleteDateIsNotNull(Pageable pageable);

    @Query("Select f from Fundraising f where f.endDate < CURRENT_DATE and f.deleteDate is null")
    List<Fundraising> findAllExpiredByDeleteDateIsNotNull();

    @Query("select f2 from Favorite f left outer join Fundraising f2 on f.fundraising.id=f2.id where f.user.id = :userId")
    Page<Fundraising> findAllFavoriteFundraisingsByUserId(Pageable pageable, Long userId);

    @Query("select f2 from Fundraising f2 where f2.user.id=:userId and f2.deleteDate is null")
    Page<Fundraising> findByUserId(Long userId, Pageable pageable);
}
