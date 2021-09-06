-- *** Product catalog ***
CREATE TABLE Product
(
  id BIGSERIAL,
  modificationCounter INTEGER NOT NULL,
  title VARCHAR (255),
  description VARCHAR (4000),
  price DECIMAL (16,10),
  CONSTRAINT PK_Product PRIMARY KEY(id)
);
