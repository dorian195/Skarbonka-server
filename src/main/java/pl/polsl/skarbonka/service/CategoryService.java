package pl.polsl.skarbonka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.skarbonka.model.Category;
import pl.polsl.skarbonka.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
