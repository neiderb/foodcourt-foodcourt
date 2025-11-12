CREATE TABLE foodcourt_order (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_client BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    id_chef BIGINT,
    id_restaurant BIGINT REFERENCES restaurant (id) NOT NULL
);

CREATE TABLE order_dish (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_order BIGINT REFERENCES foodcourt_order (id) NOT NULL,
    id_dish BIGINT REFERENCES dish (id) NOT NULL,
    quantity INT NOT NULL
);
