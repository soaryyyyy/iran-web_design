package itu.binome.backend.repository;

import itu.binome.backend.entity.Slug;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SlugRepository extends JpaRepository<Slug, Long> {

    Optional<Slug> findBySlug(String slug);

    Optional<Slug> findByTypeAndEntityId(String type, Long entityId);
}
