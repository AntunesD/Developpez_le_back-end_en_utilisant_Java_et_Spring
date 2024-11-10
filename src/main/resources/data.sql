-- Création de la table USERS
CREATE TABLE IF NOT EXISTS USERS (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255),
  name VARCHAR(255),
  password VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

-- Création de la table RENTALS
CREATE TABLE IF NOT EXISTS RENTALS (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  surface NUMERIC,
  price NUMERIC,
  picture VARCHAR(255),
  description VARCHAR(2000),
  owner_id INTEGER NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (owner_id) REFERENCES USERS(id)
);

-- Création de la table MESSAGES
CREATE TABLE IF NOT EXISTS MESSAGES (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  rental_id INTEGER,
  user_id INTEGER,
  message VARCHAR(2000),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (rental_id) REFERENCES RENTALS(id),
  FOREIGN KEY (user_id) REFERENCES USERS(id)
);

-- Index unique sur l'email des utilisateurs
CREATE UNIQUE INDEX IF NOT EXISTS USERS_index ON USERS (email);

-- Insertion de données dans la table USERS
INSERT INTO USERS (email, name, password, created_at, updated_at)
VALUES ('user1@example.com', 'User One', '$2a$10$npQMwlTbJT24NhqLv2I9gujYg9CEsrETOS4GklTeXnhMPH63YMWr6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO USERS (email, name, password, created_at, updated_at)
VALUES ('user2@example.com', 'User Two', '$2a$10$DYfMqMUFjDQMukH22WLVBeuEJb7BrUhgBUj7K2uUw4D2ud8HiBNiK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertion de données dans la table RENTALS
INSERT INTO RENTALS (name, surface, price, picture, description, owner_id, created_at, updated_at)
VALUES ('Apartment 1', 60.0, 1200.0, 'https://www.maisons-balency.fr/wp-content/uploads/2021/10/202112-construction-maisons-balency5-950x430-c-center.jpg', 'Spacious 2-bedroom apartment', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO RENTALS (name, surface, price, picture, description, owner_id, created_at, updated_at)
VALUES ('Apartment 2', 45.0, 950.0, 'https://www.villas-melrose.fr/wp-content/uploads/2023/05/quel-prix-maison-haut-gamme-2.jpg', 'Cozy studio apartment', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertion de données dans la table MESSAGES
INSERT INTO MESSAGES (rental_id, user_id, message, created_at, updated_at)
VALUES (1, 2, 'Interested in renting this apartment. Please contact me.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO MESSAGES (rental_id, user_id, message, created_at, updated_at)
VALUES (2, 1, 'I have some questions about the apartment.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
