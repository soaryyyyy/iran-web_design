-- ============================================
-- Site d'informations : Guerre en Iran
-- Base de données PostgreSQL
-- ============================================

CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    role        VARCHAR(20)  NOT NULL DEFAULT 'ADMIN',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE articles (
    id           BIGSERIAL PRIMARY KEY,
    titre        VARCHAR(255) NOT NULL,
    resume       TEXT,
    contenu      TEXT         NOT NULL,
    date_pub     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modif   TIMESTAMP,
    publie       BOOLEAN      NOT NULL DEFAULT FALSE,
    categorie_id BIGINT       REFERENCES categories(id) ON DELETE SET NULL,
    auteur_id    BIGINT       REFERENCES users(id)      ON DELETE SET NULL
);

CREATE TABLE images (
    id          BIGSERIAL PRIMARY KEY,
    url         VARCHAR(500) NOT NULL,
    alt         VARCHAR(255) NOT NULL,
    article_id  BIGINT       NOT NULL REFERENCES articles(id) ON DELETE CASCADE
);

-- ============================================
-- Table centralisée pour le URL rewriting
-- type : 'article' ou 'category'
-- entity_id : id de l'entité correspondante
-- Exemple : /article/guerre-iran  → type=article,  entity_id=1
--           /categorie/politique  → type=category, entity_id=2
-- ============================================

CREATE TABLE slugs (
    id          BIGSERIAL PRIMARY KEY,
    slug        VARCHAR(255) NOT NULL UNIQUE,
    type        VARCHAR(20)  NOT NULL CHECK (type IN ('article', 'category')),
    entity_id   BIGINT       NOT NULL
);

CREATE INDEX idx_slugs_slug ON slugs(slug);

-- ============================================
-- Données initiales
-- ============================================

INSERT INTO categories (nom, description) VALUES
    ('Politique',     'Actualités politiques et diplomatiques'),
    ('Militaire',     'Conflits et opérations militaires'),
    ('Humanitaire',   'Situation humanitaire et populations civiles'),
    ('International', 'Réactions et implications internationales'),
    ('Économie',      'Impact économique du conflit');

INSERT INTO slugs (slug, type, entity_id) VALUES
    ('politique',     'category', 1),
    ('militaire',     'category', 2),
    ('humanitaire',   'category', 3),
    ('international', 'category', 4),
    ('economie',      'category', 5);

-- Mot de passe : admin (hashé BCrypt avec BCryptPasswordEncoder de Spring)
-- Plaintext : admin
INSERT INTO users (username, password, email, role) VALUES
    ('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@iran-info.fr', 'ADMIN');
