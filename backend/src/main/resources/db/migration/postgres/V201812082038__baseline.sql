CREATE TABLE PART
(
    ID                SERIAL NOT NULL PRIMARY KEY,
    NAME              VARCHAR(50)           NOT NULL,
    COST              NUMERIC(10, 2)        NOT NULL,
    QUANTITY          SMALLINT              NOT NULL,
    SHORT_DESCRIPTION VARCHAR(255),
    DESCRIPTION       VARCHAR(4000),
    URL               VARCHAR(400),
    ORDER_DATE        TIMESTAMP,
    ORDER_ID          VARCHAR(255)
);

CREATE TABLE PART_IMAGES
(
    ID        SERIAL NOT NULL PRIMARY KEY,
    PART_ID   BIGINT,
    MEDIA_TYPE VARCHAR(255),
    CONTENT      BYTEA,
    foreign key (PART_ID) references PART (ID)
);
