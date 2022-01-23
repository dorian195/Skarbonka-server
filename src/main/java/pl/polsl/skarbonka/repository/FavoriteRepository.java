package pl.polsl.skarbonka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.polsl.skarbonka.model.Favorite;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    void deleteAllByFundraisingId(Long id);

    @Query("select f from Favorite f join Fundraising f2 on f2.id=f.fundraising.id where f2.id=:id")
    List<Favorite> findAllByFundraisingId(Long id);

    @Query("select f from Favorite f where f.fundraising.id=:id and f.user.id=:userId")
    Optional<Favorite> findByIdAndUserId(Long id, Long userId);

}
