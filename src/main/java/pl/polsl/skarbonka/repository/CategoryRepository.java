package pl.polsl.skarbonka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.skarbonka.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
