-- Tabla de imágenes
CREATE TABLE IF NOT EXISTS image (
     imageId                    SERIAL PRIMARY KEY,
     image                      BYTEA NOT NULL
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
     userId                     SERIAL PRIMARY KEY,
     username                   VARCHAR(64) UNIQUE NOT NULL,
     mail                       VARCHAR(255),
     password                   VARCHAR(255) NOT NULL,
     imageId                    INTEGER REFERENCES image(imageId),
     verificationCode           INTEGER,
     isVerified                 BOOLEAN,
     language                   VARCHAR(64)
);

-- Tabla de modelo de libros
CREATE TABLE IF NOT EXISTS book_model (
      bookModelId               SERIAL PRIMARY KEY,
      isbn                      VARCHAR(13) NOT NULL, -- UNIQUE
      title                     VARCHAR(255) NOT NULL,
      editorial                 VARCHAR(255) NOT NULL,
      description               TEXT,
      genre                     INTEGER,
      edition                   INTEGER,
      weight                    INTEGER,
      pages                     INTEGER,
      bookLanguage              INTEGER,
      dimension                 INTEGER, -- SMALL, MEDIUM, LARGE.
      publicationYear           SMALLINT,
      isPocketEdition           BOOLEAN,
      isHardcover               BOOLEAN,
      imageId                   INTEGER REFERENCES image(imageId)
);



-- Tabla de libros
CREATE TABLE IF NOT EXISTS book (
    bookId                      SERIAL PRIMARY KEY,
    bookModelId                 INTEGER REFERENCES book_model(bookModelId) ON DELETE SET NULL,
    ownerId                     INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
    bookState                   INTEGER NOT NULL,
    exchangesQty                INTEGER NOT NULL
);


-- Tabla de imágenes de libros
CREATE TABLE IF NOT EXISTS book_image (
      bookId                    INTEGER NOT NULL REFERENCES book(bookId) ON DELETE CASCADE,
      imageOrder                INTEGER NOT NULL, -- Para el orden de las fotos
      imageId                   INTEGER NOT NULL REFERENCES image(imageId) ON DELETE CASCADE,
      imageDatetime             TIMESTAMP,
      PRIMARY KEY(bookId, imageOrder)
);

-- Tabla de ratings
CREATE TABLE IF NOT EXISTS book_rating (
       ratingId     SERIAL PRIMARY KEY,
       userId       INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
       bookModelId  INTEGER NOT NULL REFERENCES book_model(bookModelId) ON DELETE CASCADE,
       rating       INTEGER
);


-- Tabla de autores
CREATE TABLE IF NOT EXISTS author (
      authorId                  SERIAL PRIMARY KEY,
      authorName                VARCHAR(255) NOT NULL -- UNIQUE
);

-- Relación libro y autor
CREATE TABLE IF NOT EXISTS book_author (
       bookModelId              INTEGER NOT NULL REFERENCES book_model(bookModelId) ON DELETE CASCADE,
       authorId                 INTEGER NOT NULL REFERENCES author(authorId) ON DELETE CASCADE,
       PRIMARY KEY (bookModelId, authorId)
);

-- Tabla de ubicaciones
CREATE TABLE IF NOT EXISTS location (
        locationId              SERIAL PRIMARY KEY,
        locationString          VARCHAR(255) NOT NULL -- UNIQUE
);

-- Tabla de publicaciones
CREATE TABLE IF NOT EXISTS publication (
       publicationId            SERIAL PRIMARY KEY,
       bookId                   INTEGER NOT NULL REFERENCES book(bookId),
       userId                   INTEGER NOT NULL REFERENCES users(userId),
       publicationState         INTEGER NOT NULL,
       publicationDatetime      TIMESTAMP,
       locationId               INTEGER REFERENCES location(locationId)
);

-- Tabla de intercambios
CREATE TABLE IF NOT EXISTS exchange (
        exchangeId              SERIAL PRIMARY KEY,
        offererPubId            INTEGER NOT NULL REFERENCES publication(publicationId),
        requesterPubId          INTEGER NOT NULL REFERENCES publication(publicationId),
        exchangeState           INTEGER NOT NULL,
        acceptCode              INTEGER NOT NULL,
        offererReceivedBook     BOOLEAN,
        requesterReceivedBook   BOOLEAN,
        exchangeEndDate         TIMESTAMP,
        exchangeStartDate       TIMESTAMP
);

-- Tabla de reseñas de usuarios
CREATE TABLE IF NOT EXISTS user_review (
       userReviewId             SERIAL PRIMARY KEY,
       exchangeId               INTEGER NOT NULL REFERENCES exchange(exchangeId),
       reviewerId               INTEGER NOT NULL REFERENCES users(userId),
       subjectId                INTEGER NOT NULL REFERENCES users(userId),
       reviewDescription        TEXT,
       reviewDate               TIMESTAMP,
       userReviewRating         INTEGER
);

-- Tabla de ubicaciones de publicaciones
CREATE TABLE IF NOT EXISTS publication_location (
      publicationId INTEGER NOT NULL,
      locationId INTEGER NOT NULL,
      PRIMARY KEY (publicationId, locationId),
      FOREIGN KEY (publicationId) REFERENCES publication(publicationId) ON DELETE CASCADE,
      FOREIGN KEY (locationId) REFERENCES location(locationId) ON DELETE SET NULL
);

-- Tabla de ubicaciones de los usuarios

-- Tabla de ubicaciones de los usuarios
CREATE TABLE IF NOT EXISTS user_location (
         userLocationId  SERIAL PRIMARY KEY,
         userId          INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
         locationId      INTEGER NOT NULL REFERENCES location(locationId) ON DELETE CASCADE,
         UNIQUE (userId, locationId)
)



