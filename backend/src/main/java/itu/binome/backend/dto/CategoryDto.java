package itu.binome.backend.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String nom;
    private String slug;
    private String description;
}
