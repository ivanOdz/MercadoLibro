CREATE TABLE IF NOT EXISTS users (
    userId              SERIAL PRIMARY KEY,
    username            VARCHAR(255) UNIQUE NOT NULL,
    mail                VARCHAR(255) UNIQUE NOT NULL,
    password            VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS images (
    imageId             SERIAL PRIMARY KEY,
    image               BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
    bookId              SERIAL PRIMARY KEY,
    isbn                VARCHAR(13) NOT NULL,
    title               VARCHAR(255) NOT NULL,
    editorial           VARCHAR(255) NOT NULL,
    description         TEXT,
    genre               INTEGER,
    bookState			INTEGER NOT NULL,
    publicationState    INTEGER NOT NULL,
    edition             INTEGER,
    rating              INTEGER,
    image               INTEGER REFERENCES images(imageId),
    owner               INTEGER REFERENCES users(userId)
);

CREATE TABLE IF NOT EXISTS author (
    authorId            SERIAL PRIMARY KEY,
    authorName          VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS book_author (
    bookId              INTEGER,
    authorId            INTEGER,
    PRIMARY KEY (bookId, authorId),
    FOREIGN KEY (bookId) REFERENCES books(bookId) ON DELETE CASCADE,
    FOREIGN KEY (authorId) REFERENCES author(authorId) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS publication (
    publicationId       SERIAL PRIMARY KEY,
    bookId              INTEGER REFERENCES books(bookId) NOT NULL,
    userId              INTEGER REFERENCES users(userId) NOT NULL,
    publicationState    INTEGER NOT NULL,
    location            VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS exchanges (
    exchangeId          SERIAL PRIMARY KEY,
    offerer             INTEGER REFERENCES publication(publicationId) NOT NULL,
    requester           INTEGER REFERENCES publication(publicationId) NOT NULL,
    exchangeState       INTEGER NOT NULL,
    acceptCode          INTEGER NOT NULL
);

