package itu.binome.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleDto {
    private Long id;
    private String titre;
    private String slug;
    private String resume;
    private String contenu;
    private LocalDateTime datePub;
    private Boolean publie;
    private String categorie;
    private String auteur;
}
