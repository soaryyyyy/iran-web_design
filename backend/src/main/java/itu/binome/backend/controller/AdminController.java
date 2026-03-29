package itu.binome.backend.controller;

import itu.binome.backend.dto.ArticleDto;
import itu.binome.backend.dto.ArticleFormDto;
import itu.binome.backend.dto.CategoryDto;
import itu.binome.backend.service.ArticleService;
import itu.binome.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Endpoints backoffice — nécessitent une authentification (Spring Security)
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ArticleService articleService;
    private final CategoryService categoryService;

    // ── Articles ──────────────────────────────────────────

    @GetMapping("/articles")
    public List<ArticleDto> getAllArticles() {
        return articleService.findAll();
    }

    @PostMapping("/articles")
    public ArticleDto createArticle(@RequestBody ArticleFormDto form) {
        return articleService.create(form);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id,
                                                     @RequestBody ArticleFormDto form) {
        return articleService.update(id, form)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Catégories ────────────────────────────────────────

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryService.findAll();
    }

    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody Map<String, String> body) {
        return categoryService.create(body.get("nom"), body.get("description"));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
