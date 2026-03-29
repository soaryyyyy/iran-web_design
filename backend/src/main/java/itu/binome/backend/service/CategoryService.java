package itu.binome.backend.service;

import itu.binome.backend.dto.CategoryDto;
import itu.binome.backend.entity.Category;
import itu.binome.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SlugService slugService;

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public CategoryDto create(String nom, String description) {
        Category category = new Category();
        category.setNom(nom);
        category.setDescription(description);
        category = categoryRepository.save(category);

        String slugValue = slugService.generate(nom);
        slugService.save(slugValue, "category", category.getId());

        return toDto(category);
    }

    public void delete(Long id) {
        slugService.deleteByTypeAndEntityId("category", id);
        categoryRepository.deleteById(id);
    }

    private CategoryDto toDto(Category c) {
        CategoryDto dto = new CategoryDto();
        dto.setId(c.getId());
        dto.setNom(c.getNom());
        dto.setDescription(c.getDescription());
        slugService.findByTypeAndEntityId("category", c.getId())
                .ifPresent(s -> dto.setSlug(s.getSlug()));
        return dto;
    }
}
