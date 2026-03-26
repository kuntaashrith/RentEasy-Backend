-- RentEasy sample MySQL schema (InnoDB)
-- Note: If you use spring.jpa.hibernate.ddl-auto=update, Hibernate will manage schema changes.

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(200) NOT NULL,
  password VARCHAR(200) NOT NULL,
  phone VARCHAR(30),
  role VARCHAR(20) NOT NULL,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  UNIQUE KEY uk_users_email (email),
  KEY idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS properties (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(160) NOT NULL,
  description TEXT,
  city VARCHAR(80) NOT NULL,
  address VARCHAR(255) NOT NULL,
  rent DECIMAL(12,2) NOT NULL,
  bhk INT NOT NULL,
  property_type VARCHAR(30) NOT NULL,
  available BIT(1) NOT NULL,
  owner_id BIGINT NOT NULL,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  KEY idx_properties_city (city),
  KEY idx_properties_rent (rent),
  KEY idx_properties_bhk (bhk),
  KEY idx_properties_owner (owner_id),
  CONSTRAINT fk_properties_owner FOREIGN KEY (owner_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS property_images (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  property_id BIGINT NOT NULL,
  image_url VARCHAR(1000) NOT NULL,
  KEY idx_property_images_property (property_id),
  CONSTRAINT fk_property_images_property FOREIGN KEY (property_id) REFERENCES properties(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS favorite_properties (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  property_id BIGINT NOT NULL,
  UNIQUE KEY uk_favorites_user_property (user_id, property_id),
  KEY idx_favorites_user (user_id),
  KEY idx_favorites_property (property_id),
  CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_favorites_property FOREIGN KEY (property_id) REFERENCES properties(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inquiries (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  property_id BIGINT NOT NULL,
  tenant_id BIGINT NOT NULL,
  message TEXT NOT NULL,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  KEY idx_inquiries_property (property_id),
  KEY idx_inquiries_tenant (tenant_id),
  CONSTRAINT fk_inquiries_property FOREIGN KEY (property_id) REFERENCES properties(id),
  CONSTRAINT fk_inquiries_tenant FOREIGN KEY (tenant_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

