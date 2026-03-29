package itu.binome.backend.dto;

import lombok.Data;

// Données reçues lors de la création/modification d'un article (backoffice)
@Data
public class ArticleFormDto {
    private String titre;
    private String resume;
    private String contenu;
    private Boolean publie;
    private Long categorieId;
}
