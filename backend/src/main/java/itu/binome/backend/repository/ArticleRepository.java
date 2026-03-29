package itu.binome.backend.repository;

import itu.binome.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Articles publiés, du plus récent au plus ancien
    List<Article> findByPublieTrueOrderByDatePubDesc();

    // Articles d'une catégorie (frontoffice)
    List<Article> findByCategorieIdAndPublieTrueOrderByDatePubDesc(Long categorieId);
}
