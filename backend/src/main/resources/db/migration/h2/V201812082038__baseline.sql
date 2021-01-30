CREATE SEQUENCE IF NOT EXISTS part_id_seq;
CREATE SEQUENCE IF NOT EXISTS part_images_id_seq;

CREATE TABLE part
(
    id                BIGINT         NOT NULL DEFAULT NEXTVAL('part_id_seq'),
    name              VARCHAR(50)    NOT NULL,
    cost              NUMERIC(10, 2) NOT NULL,
    quantity          SMALLINT       NOT NULL,
    short_description VARCHAR(255),
    description       VARCHAR(4000),
    url               VARCHAR(400),
    order_date        TIMESTAMP,
    order_id          VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE part_images
(
    id         BIGINT NOT NULL DEFAULT NEXTVAL('part_images_id_seq'),
    part_id    BIGINT,
    media_type VARCHAR(255),
    content    BLOB,
    PRIMARY KEY (id),
    FOREIGN KEY (part_id) REFERENCES part (id)
);
