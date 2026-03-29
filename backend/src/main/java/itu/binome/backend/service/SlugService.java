package itu.binome.backend.service;

import itu.binome.backend.entity.Slug;
import itu.binome.backend.repository.SlugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SlugService {

    private final SlugRepository slugRepository;

    // Génère un slug lisible à partir d'un titre
    // ex. "Guerre en Írán !" → "guerre-en-iran"
    public String generate(String titre) {
        return Normalizer.normalize(titre, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    public Slug save(String slugValue, String type, Long entityId) {
        Slug slug = new Slug();
        slug.setSlug(slugValue);
        slug.setType(type);
        slug.setEntityId(entityId);
        return slugRepository.save(slug);
    }

    public Optional<Slug> findBySlug(String slug) {
        return slugRepository.findBySlug(slug);
    }

    public Optional<Slug> findByTypeAndEntityId(String type, Long entityId) {
        return slugRepository.findByTypeAndEntityId(type, entityId);
    }

    public void deleteByTypeAndEntityId(String type, Long entityId) {
        slugRepository.findByTypeAndEntityId(type, entityId)
                .ifPresent(slugRepository::delete);
    }
}
