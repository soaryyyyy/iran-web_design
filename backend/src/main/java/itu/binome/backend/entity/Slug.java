package itu.binome.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "slugs")
public class Slug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // URL lisible : ex. "guerre-en-iran", "politique"
    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    // "article" ou "category"
    @Column(nullable = false, length = 20)
    private String type;

    // ID de l'entité cible (article ou catégorie)
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
}
