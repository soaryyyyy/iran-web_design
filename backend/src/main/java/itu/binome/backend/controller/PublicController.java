package itu.binome.backend.controller;

import itu.binome.backend.dto.ArticleDto;
import itu.binome.backend.dto.CategoryDto;
import itu.binome.backend.service.ArticleService;
import itu.binome.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Endpoints publics — FrontOffice (pas d'authentification requise)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicController {

    private final ArticleService articleService;
    private final CategoryService categoryService;

    // GET /api/articles → tous les articles publiés
    @GetMapping("/articles")
    public List<ArticleDto> getArticles() {
        return articleService.findPublished();
    }

    // GET /api/articles/{slug} → un article par son slug
    @GetMapping("/articles/{slug}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable String slug) {
        return articleService.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/categories → toutes les catégories
    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryService.findAll();
    }

    // GET /api/categories/{slug}/articles → articles d'une catégorie
    @GetMapping("/categories/{slug}/articles")
    public List<ArticleDto> getArticlesByCategory(@PathVariable String slug) {
        return articleService.findByCategory(slug);
    }
}
