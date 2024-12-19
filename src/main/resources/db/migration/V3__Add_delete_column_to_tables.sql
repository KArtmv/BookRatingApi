ALTER TABLE author
    ADD deleted BOOLEAN,
    ALTER COLUMN deleted SET NOT NULL,
    ALTER COLUMN deleted SET DEFAULT false;

ALTER TABLE book
    ADD deleted BOOLEAN,
    ALTER COLUMN deleted SET NOT NULL,
    ALTER COLUMN deleted SET DEFAULT false;

ALTER TABLE publisher
    ADD deleted BOOLEAN,
    ALTER COLUMN deleted SET NOT NULL,
    ALTER COLUMN deleted SET DEFAULT false;

ALTER TABLE rating
    ADD deleted BOOLEAN,
    ALTER COLUMN deleted SET NOT NULL,
    ALTER COLUMN deleted SET DEFAULT false;

ALTER TABLE users
    ADD deleted BOOLEAN,
    ALTER COLUMN deleted SET NOT NULL,
    ALTER COLUMN deleted SET DEFAULT false;