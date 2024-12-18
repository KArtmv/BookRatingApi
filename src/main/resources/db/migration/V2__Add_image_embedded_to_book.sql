ALTER TABLE book
    DROP CONSTRAINT fk_book_on_image;

ALTER TABLE book
    ADD image_url_large  VARCHAR(255),
    ADD image_url_medium VARCHAR(255),
    ADD image_url_small  VARCHAR(255);

DROP TABLE image CASCADE;

ALTER TABLE book
    DROP COLUMN image_id;

DROP SEQUENCE image_id_seq CASCADE;