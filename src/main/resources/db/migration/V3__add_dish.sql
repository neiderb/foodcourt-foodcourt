CREATE TABLE dish (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    id_category BIGINT REFERENCES category (id) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(20, 0) NOT NULL,
    id_restaurant BIGINT REFERENCES restaurant (id) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE NOT NULL
);