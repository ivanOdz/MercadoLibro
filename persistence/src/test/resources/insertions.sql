DO $$
DECLARE
        myBookModelId INTEGER;
        myUserId INTEGER;
        mySecondUserId INTEGER;
        myBookId INTEGER;
        mySecondBookId INTEGER;
        myPublicationId INTEGER;
        mySecondPublicationId INTEGER;
        myLocationId INTEGER;
        myAuthorId INTEGER;
        myExchangeId INTEGER;
        
BEGIN

-- Insertar Usuarios ---------------------------------------------------------------------------------------------------
        
        IF NOT EXISTS (SELECT userName FROM users WHERE mail = 'modzomek@itba.edu.ar') THEN
                INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
                VALUES ('ivan', 'modzomek@itba.edu.ar', '', NULL, NULL, TRUE);
        END IF;
        
        
        IF NOT EXISTS (SELECT userName FROM users WHERE mail = 'jtechenski@itba.edu.ar') THEN
                INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
                VALUES ('juli', 'jtechenski@itba.edu.ar', '', NULL, NULL, TRUE);
        END IF;
        
        
        IF NOT EXISTS (SELECT userName FROM users WHERE mail = 'mtaurian@itba.edu.ar') THEN
                INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
                VALUES ('maggie', 'mtaurian@itba.edu.ar', '', NULL, NULL, TRUE);
        END IF;
        
        
        IF NOT EXISTS (SELECT userName FROM users WHERE mail = 'tscheffer@itba.edu.ar') THEN
                INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
                VALUES ('tomas', 'tscheffer@itba.edu.ar', '', NULL, NULL, TRUE);
        END IF;

-- Insertar Modelo de Libro ---------------------------------------------------------------------------------------------------

-- 1 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9780140035247') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788497592208', 'Cien años de soledad', 'Sudamericana', 'Una novela emblemática del realismo mágico que narra la historia de la familia Buendía a lo largo de varias generaciones.', 2, 1, 600, 471, 1, 1, 1967, FALSE, TRUE, NULL);
        END IF;
        
-- 2 --        
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9788481093353') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788491050568', 'Don Quijote de la Mancha', 'Real Academia Española', 'La obra maestra de la literatura española que sigue las aventuras del caballero Don Quijote y su escudero Sancho Panza.', 3, 1, 800, 1000, 1, 2, 1605, TRUE, FALSE, NULL);
        END IF;

-- 3 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9780307472595') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788408186206', 'La sombra del viento', 'Planeta', 'Una novela fascinante ambientada en la Barcelona de la posguerra que mezcla misterio, amor y tragedia.', 4, 2, 400, 575, 1, 1, 2001, TRUE, FALSE, NULL);
        END IF;

-- 4 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9780141023472') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788497592451', 'El amor en los tiempos del cólera', 'Sudamericana', 'Una historia de amor que se extiende a lo largo de más de 50 años, escrita por el ganador del Premio Nobel.', 5, 1, 500, 490, 1, 1, 1985, FALSE, TRUE, NULL);
        END IF;

-- 5 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9780060834838') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9780062315007', 'El alquimista', 'HarperCollins', 'Una novela filosófica sobre un joven pastor que sigue su sueño de encontrar un tesoro en Egipto.', 6, 3, 300, 208, 1, 1, 1988, TRUE, FALSE, NULL);
        END IF;

-- 6 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9788437604947') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788437604947', 'La casa de los espíritus', 'Debolsillo', 'Una obra que recorre las vidas de varias generaciones de la familia Trueba, desde lo fantástico hasta lo histórico.', 7, 2, 500, 448, 1, 1, 1982, FALSE, TRUE, NULL);
        END IF;

-- 7 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9788420471839') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788420471839', 'Crónica de una muerte anunciada', 'Debolsillo', 'Un relato corto sobre la muerte premeditada de Santiago Nasar, basado en hechos reales.', 2, 1, 300, 128, 1, 1, 1981, TRUE, FALSE, NULL);
        END IF;

-- 8 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9788497594319') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788497594319', 'Los detectives salvajes', 'Anagrama', 'Una novela sobre la vida bohemia de poetas en el México de los años 70.', 8, 1, 700, 609, 1, 1, 1998, FALSE, TRUE, NULL);
        END IF;
        
-- 9 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9788423353248') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788423353248', 'Patria', 'Tusquets', 'Una novela que explora las vidas de dos familias vascas en el contexto del conflicto de ETA.', 9, 1, 800, 648, 1, 1, 2016, FALSE, TRUE, NULL);
        END IF;

-- 10 --
        IF NOT EXISTS (SELECT isbn FROM book_model WHERE isbn = '9788445002132') THEN
                INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
                VALUES ('9788445002132', 'La ciudad y los perros', 'Alfaguara', 'Una obra clave de la literatura latinoamericana que sigue a los estudiantes de un internado militar en el Perú.', 3, 2, 500, 384, 1, 1, 1962, FALSE, TRUE, NULL);
        END IF;

-- Insertar autores ---------------------------------------------------------------------------------------------------
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Gabriel García Márquez') THEN
                INSERT INTO author (authorName)
                VALUES ('Gabriel García Márquez');
        END IF;
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Miguel de Cervantes') THEN
                INSERT INTO author (authorName)
                VALUES ('Miguel de Cervantes');
        END IF;
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Carlos Ruiz Zafón') THEN
                INSERT INTO author (authorName)
                VALUES ('Carlos Ruiz Zafón');
        END IF;
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Paulo Coelho') THEN
                INSERT INTO author (authorName)
                VALUES ('Paulo Coelho');
        END IF;
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Isabel Allende') THEN
                INSERT INTO author (authorName)
                VALUES ('Isabel Allende');
        END IF;
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Roberto Bolaño') THEN
                INSERT INTO author (authorName)
                VALUES ('Roberto Bolaño');
        END IF;
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Fernando Aramburu') THEN
                INSERT INTO author (authorName)
                VALUES ('Fernando Aramburu');
        END IF;
        
        IF NOT EXISTS (SELECT authorName FROM author WHERE authorName LIKE 'Mario Vargas Llosa') THEN
                INSERT INTO author (authorName)
                VALUES ('Mario Vargas Llosa');
        END IF;
        
-- Insertar ubicación ---------------------------------------------------------------------------------------------------

        IF NOT EXISTS (SELECT locationString FROM location WHERE locationString LIKE 'Zona Norte') THEN
                INSERT INTO location (locationString)
                VALUES ('Zona Norte');
        END IF;
        
        
        IF NOT EXISTS (SELECT locationString FROM location WHERE locationString LIKE 'Zona Sur') THEN
                INSERT INTO location (locationString)
                VALUES ('Zona Sur');
        END IF;
        

-- AUTORES ------------------------------------------------------------------------------------------------------------------

-- 1 -- Cien años de soledad

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788497592208';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Gabriel García Márquez';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;

-- 2 -- Don Quijote de la Mancha

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788491050568';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Miguel de Cervantes';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 3 -- La sombra del viento

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788408186206';

        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Carlos Ruiz Zafón';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 4 -- El amor en los tiempos del cólera

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788497592451';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Gabriel García Márquez';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 5 -- El alquimista

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9780062315007';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Paulo Coelho';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 6 -- La casa de los espíritus

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788437604947';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Isabel Allende';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 7 -- Crónica de una muerte anunciada

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788420471839';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Gabriel García Márquez';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 8 -- Los detectives salvajes

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788497594319';

        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Roberto Bolaño';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 9 -- Patria

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788423353248';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Fernando Aramburu';
                
                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- 10 -- La ciudad y los perros

        SELECT bookModelId INTO myBookModelId
        FROM book_model
        WHERE isbn = '9788445002132';
        
        IF FOUND THEN
                SELECT authorId INTO myAuthorId
                FROM author
                WHERE authorName = 'Mario Vargas Llosa';

                IF FOUND THEN
                        INSERT INTO book_author (bookModelId, authorId)
                        VALUES (myBookModelId, myAuthorId) ON CONFLICT (bookModelId, authorId) DO NOTHING;
                END IF;
        END IF;
        
-- LIBROS + REVIEWS ----------------------------------------------------------------------------------------------------------------
        
-- Insertar Libros para Ivan --

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'modzomek@itba.edu.ar'
        LIMIT 1;
        
        IF FOUND THEN
        -- 1° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788445002132'; -- La ciudad y los perros
                
                IF FOUND THEN
                
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 1, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 4) ON CONFLICT (userId, bookModelId) DO NOTHING;
                                
                        END IF;
                END IF;
                
        -- 2° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788423353248'; -- Patria
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 2, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 3) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
        -- 3° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788497594319'; -- Los detectives salvajes
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 3, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 4) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
                
        -- 4° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788420471839'; -- Crónica de una muerte anunciada
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 4, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 5) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
        END IF;
        
-- Insertar Libros para Juli --

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'jtechenski@itba.edu.ar'
        LIMIT 1;
 
        IF FOUND THEN
        -- 1° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788437604947'; -- La casa de los espíritus
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 1, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 3) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
        
        -- 2° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9780062315007'; -- El alquimista
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 2, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 4) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
        END IF;
        
-- Insertar Libros para Maggie --

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'mtaurian@itba.edu.ar'
        LIMIT 1;
        
        IF FOUND THEN
        -- 1° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788497592451'; -- El amor en los tiempos del cólera
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 3, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 2) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
        
        -- 2° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788408186206'; -- La sombra del viento
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 4, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 5) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
                
                
        END IF;
        
-- Insertar Libros para Tom's --
        
        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'tscheffer@itba.edu.ar'
        LIMIT 1;

        IF FOUND THEN
        -- 1° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788491050568'; -- Don Quijote de la Mancha
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 3, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 4) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
        
        -- 2° --
                SELECT bookModelId INTO myBookModelId
                FROM book_model
                WHERE isbn = '9788497592208'; -- Cien años de soledad 
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT bookId FROM book WHERE bookModelid = myBookModelId)
                        THEN
                                INSERT INTO book (bookModelid, ownerId, bookState, exchangesQty)
                                VALUES (myBookModelid, myUserId, 2, 0);
                                
                                INSERT INTO book_rating (userId, bookModelId, rating)
                                VALUES (myUserId, myBookModelId, 5) ON CONFLICT (userId, bookModelId) DO NOTHING;
                        END IF;
                END IF;
        END IF;
        
-- PUBLICACIONES -------------------------------------------------------------------------------------------------------------
        
        SELECT locationId INTO myLocationId
        FROM location
        WHERE locationString LIKE 'Zona Sur';
        
-- De ivan --
        
        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'modzomek@itba.edu.ar';

        IF FOUND THEN
        
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788445002132'); -- La ciudad y los perros
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId) -- Solo me importa que apresca al menos una
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-15 10:00:00', myLocationId);
                        END IF;
                END IF;
                
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788423353248'); -- Patria
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-15 12:05:00', myLocationId);
                        END IF;
                END IF;
                
                SELECT locationId INTO myLocationId
                FROM location
                WHERE locationString LIKE 'Zona Norte';
                
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788497594319'); -- Los detectives salvajes
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-15 14:30:00', myLocationId);
                        END IF;
                END IF;
                
                                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788420471839'); -- Crónica de una muerte anunciada
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-15 16:40:05', myLocationId);
                        END IF;
                END IF;
                
        END IF;
        
-- De juli --

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'jtechenski@itba.edu.ar';

        IF FOUND THEN
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788437604947'); -- La casa de los espíritus
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-12 18:30:00', myLocationId);
                        END IF;
                END IF;
                
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9780062315007'); -- El alquimista
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-13 09:00:00', myLocationId);
                        END IF;
                END IF;
        END IF;
        
-- De maggie --

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'mtaurian@itba.edu.ar';

        IF FOUND THEN
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788497592451'); -- El amor en los tiempos del cólera
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-11 14:45:00', myLocationId);
                        END IF;
                END IF;
                
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788408186206'); -- La sombra del viento
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-15 01:00:00', myLocationId);
                        END IF;
                END IF;
        END IF;
        
-- De tom's --

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'tscheffer@itba.edu.ar';

        IF FOUND THEN
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788491050568'); -- Don Quijote de la Mancha
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-13 12:15:00', myLocationId);
                        END IF;
                END IF;
                
                SELECT bookId INTO myBookId
                FROM book
                WHERE bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788497592208'); -- Cien años de soledad 
                
                IF FOUND THEN
                        IF NOT EXISTS (SELECT publicationId FROM publication WHERE bookId = myBookId AND userId = myUserId)
                        THEN
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, myUserId, 1, '2024-09-14 20:00:00', myLocationId);
                        END IF;
                END IF;
        END IF;

-- INTERCAMBIOS + REVIEW ----------------------------------------------------------------------------------------------------

        SELECT locationId INTO myLocationId
        FROM location
        WHERE locationString LIKE 'Zona Norte';
        
-- 1: Ivan con Juli
        
        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'modzomek@itba.edu.ar';

        SELECT userId INTO mySecondUserId
        FROM users
        WHERE mail LIKE 'jtechenski@itba.edu.ar';
        
        IF (myUserId IS NOT NULL AND mySecondUserId IS NOT NULL) THEN
                
                SELECT bookId INTO myBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = myUserId AND isbn = '9788445002132'; -- La ciudad y los perros (Ivan)
                SELECT bookId INTO mySecondBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = mySecondUserId AND isbn = '9788437604947'; -- La casa de los espíritus (Juli)
                
                SELECT publicationId INTO myPublicationId
                FROM publication
                WHERE bookId = myBookId AND publicationState = 1;
                
                SELECT publicationId INTO mySecondPublicationId
                FROM publication
                WHERE bookId = mySecondBookId;
                
                IF (myBookId IS NOT NULL AND mySecondBookId IS NOT NULL AND myPublicationId IS NOT NULL AND mySecondPublicationId IS NOT NULL) THEN
                        
                        -- Actualizar publicacion Juli (pasa offered)
                        UPDATE publication SET publicationState = 2 WHERE publicationId = mySecondPublicationId;
                        
                        -- Insertar intercambio
                        INSERT INTO exchange (offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)
                        VALUES (myPublicationId, mySecondPublicationId, 1, 123456789, TRUE, TRUE, '2024-09-15 00:00:00', '2024-09-15 01:00:00')
                        RETURNING exchangeId INTO myExchangeId;
                        
                        IF myExchangeId IS NOT NULL THEN
                        
                                -- Actualizar libros
                                UPDATE book SET ownerId = mySecondUserId, exchangesQty = exchangesQty + 1 WHERE bookId = myBookId;
                                UPDATE book SET ownerId = myUserId, exchangesQty = exchangesQty + 1 WHERE bookId = mySecondBookId;
                                
                                -- Actualizar publicaciones (pasa a terminated)
                                UPDATE publication SET publicationState = 0 WHERE publicationId = myPublicationId OR publicationId = mySecondPublicationId;
                                
                                -- Insertar nueva publicacion de Juli
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, mySecondUserId, 1, '2024-09-15 08:00:00', myLocationId);
                                
                                -- Insertar nueva publicacion de Ivan
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (mySecondBookId, myUserId, 1, '2024-09-15 09:00:00', myLocationId);
                                
                                -- Insertar Review de Ivan
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, myUserId, mySecondUserId, 'Nana excelente todo', '2024-09-15 08:30:00', 5);
                                
                                -- Insertar Review de Juli
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, mySecondUserId, myUserId, 'Correcto mundo', '2024-09-15 07:45:00', 5);
                                
                        END IF;
                        
                END IF;
                
        END IF;
        
        
-- 2: Juli con Maggie
        
        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'jtechenski@itba.edu.ar';

        SELECT userId INTO mySecondUserId
        FROM users
        WHERE mail LIKE 'mtaurian@itba.edu.ar';
        
        IF (myUserId IS NOT NULL AND mySecondUserId IS NOT NULL) THEN
                
                SELECT bookId INTO myBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = myUserId AND isbn = '9780062315007'; -- El alquimista (Juli)
                SELECT bookId INTO mySecondBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = mySecondUserId AND isbn = '9788497592451'; -- El amor en los tiempos del cólera (Maggie)
                
                SELECT publicationId INTO myPublicationId
                FROM publication
                WHERE bookId = myBookId AND publicationState = 1;
                
                SELECT publicationId INTO mySecondPublicationId
                FROM publication
                WHERE bookId = mySecondBookId;

                IF (myBookId IS NOT NULL AND mySecondBookId IS NOT NULL AND myPublicationId IS NOT NULL AND mySecondPublicationId IS NOT NULL) THEN
                
                        -- Actualizar publicacion Maggie (pasa offered)
                        UPDATE publication SET publicationState = 2 WHERE publicationId = mySecondPublicationId;
                        
                        -- Insertar intercambio
                        INSERT INTO exchange (offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)
                        VALUES (myPublicationId, mySecondPublicationId, 1, 987654321, TRUE, TRUE, '2024-09-15 10:00:00', '2024-09-15 11:30:00')
                        RETURNING exchangeId INTO myExchangeId;
                        
                        IF myExchangeId IS NOT NULL THEN
                        
                                -- Actualizar libros
                                UPDATE book SET ownerId = mySecondUserId, exchangesQty = exchangesQty + 1 WHERE bookId = myBookId;
                                UPDATE book SET ownerId = myUserId, exchangesQty = exchangesQty + 1 WHERE bookId = mySecondBookId;
                                
                                -- Actualizar publicaciones (pasa a terminated)
                                UPDATE publication SET publicationState = 0 WHERE publicationId = myPublicationId OR publicationId = mySecondPublicationId;
                                
                                -- Insertar nueva publicacion de Maggie
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, mySecondUserId, 1, '2024-09-15 12:00:00', myLocationId);
                                
                                -- Insertar nueva publicacion de Juli
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (mySecondBookId, myUserId, 1, '2024-09-15 12:45:00', myLocationId);
                                
                                -- Insertar Review de Juli
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, myUserId, mySecondUserId, 'Recomiendado 100%', '2024-09-15 12:50:00', 5);
                                
                                -- Insertar Review de Maggie
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, mySecondUserId, myUserId, 'Excelente, muy bien, bárbaro', '2024-09-15 13:15:00', 5);
                                
                        END IF;
                        
                END IF;
                
        END IF;

-- 3: Maggie con Tomi

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'mtaurian@itba.edu.ar';

        SELECT userId INTO mySecondUserId
        FROM users
        WHERE mail LIKE 'tscheffer@itba.edu.ar';
        
        IF (myUserId IS NOT NULL AND mySecondUserId IS NOT NULL) THEN
                
                SELECT bookId INTO myBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = myUserId AND isbn = '9780062315007'; -- El alquimista (Maggie)
                SELECT bookId INTO mySecondBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = mySecondUserId AND isbn = '9788491050568'; -- Don Quijote de la Mancha (Tomi)
                
                SELECT publicationId INTO myPublicationId
                FROM publication
                WHERE bookId = myBookId AND publicationState = 1;
                
                SELECT publicationId INTO mySecondPublicationId
                FROM publication
                WHERE bookId = mySecondBookId;
                
                IF (myBookId IS NOT NULL AND mySecondBookId IS NOT NULL AND myPublicationId IS NOT NULL AND mySecondPublicationId IS NOT NULL) THEN
                
                        -- Actualizar publicacion Tomi (pasa offered)
                        UPDATE publication SET publicationState = 2 WHERE publicationId = mySecondPublicationId;
                        
                        -- Insertar intercambio
                        INSERT INTO exchange (offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)
                        VALUES (myPublicationId, mySecondPublicationId, 1, 678954321, TRUE, TRUE, '2024-09-15 13:00:00', '2024-09-15 16:50:00')
                        RETURNING exchangeId INTO myExchangeId;
                        
                        IF myExchangeId IS NOT NULL THEN
                        
                                -- Actualizar libros
                                UPDATE book SET ownerId = mySecondUserId, exchangesQty = exchangesQty + 1 WHERE bookId = myBookId;
                                UPDATE book SET ownerId = myUserId, exchangesQty = exchangesQty + 1 WHERE bookId = mySecondBookId;
                                
                                -- Actualizar publicaciones
                                UPDATE publication SET publicationState = 0 WHERE publicationId = myPublicationId OR publicationId = mySecondPublicationId;
                                
                                -- Insertar nueva publicacion de Tomi
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, mySecondUserId, 1, '2024-09-15 17:00:00', myLocationId);
                                
                                -- Insertar nueva publicacion de Maggie
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (mySecondBookId, myUserId, 1, '2024-09-15 18:15:00', myLocationId);
                                
                                -- Insertar Review de Maggie
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, myUserId, mySecondUserId, 'El libro me gustó', '2024-09-15 20:10:00', 4);
                                
                                -- Insertar Review de Tomi
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, mySecondUserId, myUserId, 'Buen intercambio', '2024-09-15 22:15:00', 5);
                                
                        END IF;
                        
                END IF;
                
        END IF;

-- Tomi con Ivan x 2

        SELECT userId INTO myUserId
        FROM users
        WHERE mail LIKE 'tscheffer@itba.edu.ar';

        SELECT userId INTO mySecondUserId
        FROM users
        WHERE mail LIKE 'modzomek@itba.edu.ar';
        
        IF (myUserId IS NOT NULL AND mySecondUserId IS NOT NULL) THEN
                
                SELECT bookId INTO myBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = myUserId AND isbn = '9780062315007'; -- El alquimista (Tomi)
                SELECT bookId INTO mySecondBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = mySecondUserId AND isbn = '9788420471839'; -- Crónica de una muerte anunciada (Ivan)
                
                SELECT publicationId INTO myPublicationId
                FROM publication
                WHERE bookId = myBookId AND publicationState = 1;
                
                SELECT publicationId INTO mySecondPublicationId
                FROM publication
                WHERE bookId = mySecondBookId;
                
                IF (myBookId IS NOT NULL AND mySecondBookId IS NOT NULL AND myPublicationId IS NOT NULL AND mySecondPublicationId IS NOT NULL) THEN
                
                        -- Actualizar publicacion Ivan (pasa offered)
                        UPDATE publication SET publicationState = 2 WHERE publicationId = mySecondPublicationId;
                        
                        -- Insertar intercambio
                        INSERT INTO exchange (offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)
                        VALUES (myPublicationId, mySecondPublicationId, 1, 456789123, TRUE, TRUE, '2024-09-16 08:00:00', '2024-09-16 11:30:00')
                        RETURNING exchangeId INTO myExchangeId;
                        
                        IF myExchangeId IS NOT NULL THEN
                        
                                -- Actualizar libros
                                UPDATE book SET ownerId = mySecondUserId, exchangesQty = exchangesQty + 1 WHERE bookId = myBookId;
                                UPDATE book SET ownerId = myUserId, exchangesQty = exchangesQty + 1 WHERE bookId = mySecondBookId;
                                
                                -- Actualizar publicaciones
                                UPDATE publication SET publicationState = 0 WHERE publicationId = myPublicationId OR publicationId = mySecondPublicationId;
                                
                                -- Insertar nueva publicacion de Ivan
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, mySecondUserId, 1, '2024-09-16 12:00:00', myLocationId);
                                
                                -- Insertar nueva publicacion de Tomi
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (mySecondBookId, myUserId, 1, '2024-09-16 13:12:00', myLocationId);
                                
                                -- Insertar Review de Tomi
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, myUserId, mySecondUserId, 'Todo manchado me lo entrega...', '2024-09-16 12:50:00', 2);
                                
                                -- Insertar Review de Ivan
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, mySecondUserId, myUserId, 'Me puntuo mal; pero me hice de un libro sin cafe encima', '2024-09-16 13:15:00', 3);
                                
                        END IF;
                        
                END IF;
                
                SELECT bookId INTO myBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = myUserId AND isbn = '9788497592208'; -- Cien años de soledad (Tomi)
                SELECT bookId INTO mySecondBookId FROM book JOIN book_model ON book.bookModelId = book_model.bookModelId WHERE ownerId = mySecondUserId AND isbn = '9788423353248'; -- Patria (Ivan)
                
                SELECT publicationId INTO myPublicationId
                FROM publication
                WHERE bookId = myBookId AND publicationState = 1;
                
                SELECT publicationId INTO mySecondPublicationId
                FROM publication
                WHERE bookId = mySecondBookId;
                
                IF (myBookId IS NOT NULL AND mySecondBookId IS NOT NULL AND myPublicationId IS NOT NULL AND mySecondPublicationId IS NOT NULL) THEN
                
                        -- Actualizar publicacion Ivan (pasa offered)
                        UPDATE publication SET publicationState = 2 WHERE publicationId = mySecondPublicationId;
                        
                        -- Insertar intercambio
                        INSERT INTO exchange (offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)
                        VALUES (myPublicationId, mySecondPublicationId, 1, 698745123, TRUE, TRUE, '2024-09-16 15:00:00', '2024-09-16 19:30:00')
                        RETURNING exchangeId INTO myExchangeId;
                        
                        IF myExchangeId IS NOT NULL THEN
                        
                                -- Actualizar libros
                                UPDATE book SET ownerId = mySecondUserId, exchangesQty = exchangesQty + 1 WHERE bookId = myBookId;
                                UPDATE book SET ownerId = myUserId, exchangesQty = exchangesQty + 1 WHERE bookId = mySecondBookId;
                                
                                -- Actualizar publicaciones
                                UPDATE publication SET publicationState = 2 WHERE publicationId = myPublicationId OR publicationId = mySecondPublicationId;
                                
                                -- Insertar nueva publicacion de Ivan
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (myBookId, mySecondUserId, 1, '2024-09-16 20:00:00', myLocationId);
                                
                                -- Insertar nueva publicacion de Tomi
                                INSERT INTO publication (bookId, userId, publicationState, publicationDatetime, locationId)
                                VALUES (mySecondBookId, myUserId, 1, '2024-09-16 21:00:00', myLocationId);
                                
                                -- Insertar Review de Tomi
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, myUserId, mySecondUserId, 'Que se yo, estaria bien', '2024-09-16 22:35:00', 4);
                                
                                -- Insertar Review de Ivan
                                INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating)
                                VALUES (myExchangeId, mySecondUserId, myUserId, 'Ponele', '2024-09-16 22:15:00', 4);
                                
                        END IF;
                        
                END IF;
                
        END IF;

-- FIN -----------------------------------------------------------------------------------------------------------------------

END $$;