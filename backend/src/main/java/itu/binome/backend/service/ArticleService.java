package itu.binome.backend.service;

import itu.binome.backend.dto.ArticleDto;
import itu.binome.backend.dto.ArticleFormDto;
import itu.binome.backend.entity.Article;
import itu.binome.backend.entity.Category;
import itu.binome.backend.repository.ArticleRepository;
import itu.binome.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final SlugService slugService;

    // Frontoffice : articles publiés
    public List<ArticleDto> findPublished() {
        return articleRepository.findByPublieTrueOrderByDatePubDesc()
                .stream().map(this::toDto).toList();
    }

    // Frontoffice : articles d'une catégorie via slug
    public List<ArticleDto> findByCategory(String categorySlug) {
        return slugService.findBySlug(categorySlug)
                .filter(s -> "category".equals(s.getType()))
                .map(s -> articleRepository
                        .findByCategorieIdAndPublieTrueOrderByDatePubDesc(s.getEntityId())
                        .stream().map(this::toDto).toList())
                .orElse(List.of());
    }

    // Frontoffice : article par slug
    public Optional<ArticleDto> findBySlug(String slug) {
        return slugService.findBySlug(slug)
                .filter(s -> "article".equals(s.getType()))
                .flatMap(s -> articleRepository.findById(s.getEntityId()))
                .filter(Article::getPublie)
                .map(this::toDto);
    }

    // Backoffice : tous les articles (publiés ou non)
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream().map(this::toDto).toList();
    }

    // Backoffice : créer un article
    public ArticleDto create(ArticleFormDto form) {
        Article article = new Article();
        article.setTitre(form.getTitre());
        article.setResume(form.getResume());
        article.setContenu(form.getContenu());
        article.setPublie(Boolean.TRUE.equals(form.getPublie()));

        if (form.getCategorieId() != null) {
            categoryRepository.findById(form.getCategorieId())
                    .ifPresent(article::setCategorie);
        }

        article = articleRepository.save(article);
        String slugValue = slugService.generate(form.getTitre());
        slugService.save(slugValue, "article", article.getId());

        return toDto(article);
    }

    // Backoffice : modifier un article
    public Optional<ArticleDto> update(Long id, ArticleFormDto form) {
        return articleRepository.findById(id).map(article -> {
            article.setTitre(form.getTitre());
            article.setResume(form.getResume());
            article.setContenu(form.getContenu());
            article.setPublie(Boolean.TRUE.equals(form.getPublie()));
            article.setDateModif(LocalDateTime.now());

            if (form.getCategorieId() != null) {
                categoryRepository.findById(form.getCategorieId())
                        .ifPresent(article::setCategorie);
            }

            // Mise à jour du slug
            slugService.deleteByTypeAndEntityId("article", id);
            slugService.save(slugService.generate(form.getTitre()), "article", id);

            return toDto(articleRepository.save(article));
        });
    }

    // Backoffice : supprimer un article
    public void delete(Long id) {
        slugService.deleteByTypeAndEntityId("article", id);
        articleRepository.deleteById(id);
    }

    private ArticleDto toDto(Article a) {
        ArticleDto dto = new ArticleDto();
        dto.setId(a.getId());
        dto.setTitre(a.getTitre());
        dto.setResume(a.getResume());
        dto.setContenu(a.getContenu());
        dto.setDatePub(a.getDatePub());
        dto.setPublie(a.getPublie());
        if (a.getCategorie() != null) dto.setCategorie(a.getCategorie().getNom());
        if (a.getAuteur() != null)    dto.setAuteur(a.getAuteur().getUsername());
        slugService.findByTypeAndEntityId("article", a.getId())
                .ifPresent(s -> dto.setSlug(s.getSlug()));
        return dto;
    }
}
