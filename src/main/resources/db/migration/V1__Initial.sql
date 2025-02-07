CREATE SEQUENCE IF NOT EXISTS author_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS book_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS image_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS publisher_is_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS rating_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS user_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE author
(
    id   BIGINT       NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_author PRIMARY KEY (id)
);

CREATE TABLE book
(
    id               BIGINT      NOT NULL,
    isbn             VARCHAR(13) NOT NULL,
    title            VARCHAR(255) NOT NULL,
    publication_year INTEGER     NOT NULL,
    publisher_id     BIGINT      NOT NULL,
    image_id         BIGINT,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

CREATE TABLE book_authors
(
    author_id BIGINT  NOT NULL,
    book_isbn VARCHAR NOT NULL,
    CONSTRAINT pk_book_authors PRIMARY KEY (author_id, book_isbn)
);

CREATE TABLE image
(
    id               BIGINT NOT NULL,
    image_url_small  VARCHAR(255),
    image_url_medium VARCHAR(255),
    image_url_large  VARCHAR(255),
    CONSTRAINT pk_image PRIMARY KEY (id)
);

CREATE TABLE publisher
(
    id   BIGINT       NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_publisher PRIMARY KEY (id)
);

CREATE TABLE rating
(
    id          BIGINT NOT NULL,
    book_id     BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    book_rating INTEGER,
    CONSTRAINT pk_rating PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT NOT NULL,
    location VARCHAR(255),
    age      INTEGER,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE author
    ADD CONSTRAINT uc_author_name UNIQUE (name);

ALTER TABLE book_authors
    ADD CONSTRAINT uc_book_author UNIQUE (book_isbn, author_id);

ALTER TABLE book
    ADD CONSTRAINT uc_book_image UNIQUE (image_id);

ALTER TABLE book
    ADD CONSTRAINT uc_book_isbn UNIQUE (isbn);

ALTER TABLE publisher
    ADD CONSTRAINT uc_publisher_name UNIQUE (name);

ALTER TABLE rating
    ADD CONSTRAINT uc_rating_book_id_user_id UNIQUE (book_id, user_id);

ALTER TABLE book
    ADD CONSTRAINT FK_BOOK_ON_IMAGE FOREIGN KEY (image_id) REFERENCES image (id);

ALTER TABLE book
    ADD CONSTRAINT FK_BOOK_ON_PUBLISHER FOREIGN KEY (publisher_id) REFERENCES publisher (id);

ALTER TABLE rating
    ADD CONSTRAINT FK_RATING_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id);

ALTER TABLE rating
    ADD CONSTRAINT FK_RATING_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE book_authors
    ADD CONSTRAINT fk_booaut_on_author FOREIGN KEY (author_id) REFERENCES author (id);

ALTER TABLE book_authors
    ADD CONSTRAINT fk_booaut_on_book FOREIGN KEY (book_isbn) REFERENCES book (isbn);