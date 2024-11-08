	    
-- Insertar Usuarios -------------------------------------------------------------------------------------------------------

-- 1 --
        INSERT INTO users (userId, userName, mail, password, imageId, verificationCode, isVerified, language, favoriteLocation)
        VALUES (1, 'Ivan', 'modzomek@itba.edu.ar', 'password', NULL, 123456789, TRUE, 'en', NULL);
        
-- 2 --
        INSERT INTO users (userId, userName, mail, password, imageId, verificationCode, isVerified, language, favoriteLocation)
        VALUES (2, 'Juli', 'jtechenski@itba.edu.ar', 'password', NULL, 987654321, TRUE, 'en', NULL);

-- 3 --
        INSERT INTO users (userId, userName, mail, password, imageId, verificationCode, isVerified, language, favoriteLocation)
        VALUES (3, 'Maggie', 'mtaurian@itba.edu.ar', 'drowssap', NULL, 300000000, TRUE, 'es', NULL);

-- 4 --
        INSERT INTO users (userId, userName, mail, password, imageId, verificationCode, isVerified, language, favoriteLocation)
        VALUES (4, 'Tomas', 'tscheffer@itba.edu.ar', 'drowssap', NULL, 400000000, TRUE, 'es', NULL);

-- Insertar Modelos de libros ----------------------------------------------------------------------------------------------

-- 1 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (1, '9788497592208', 'Cien años de soledad', 'Sudamericana', 'Una novela emblemática del realismo mágico que narra la historia de la familia Buendía a lo largo de varias generaciones.', 'FANTASY', 1, 600, 471, 'SPANISH', 'MEDIUM', 1967, FALSE, TRUE, NULL);
    
-- 2 --        
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (2, '9788491050568', 'Don Quijote de la Mancha', 'Real Academia Española', 'La obra maestra de la literatura española que sigue las aventuras del caballero Don Quijote y su escudero Sancho Panza.', 'CLASSIC', 1, 800, 1000, 'SPANISH', 'LARGE', 1605, TRUE, FALSE, NULL);

-- 3 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (3, '9788408186206', 'La sombra del viento', 'Planeta', 'Una novela fascinante ambientada en la Barcelona de la posguerra que mezcla misterio, amor y tragedia.', 'OTHER', 2, 400, 575, 'SPANISH', 'SMALL', 2001, TRUE, FALSE, NULL);

-- 4 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (4, '9788497592451', 'El amor en los tiempos del cólera', 'Sudamericana', 'Una historia de amor que se extiende a lo largo de más de 50 años, escrita por el ganador del Premio Nobel.', 'OTHER', 1, 500, 490, 'SPANISH', 'MEDIUM', 1985, FALSE, TRUE, NULL);

-- 5 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (5, '9780062315007', 'El alquimista', 'HarperCollins', 'Una novela filosófica sobre un joven pastor que sigue su sueño de encontrar un tesoro en Egipto.', 'OTHER', 3, 300, 208, 'SPANISH', 'SMALL', 1988, TRUE, FALSE, NULL);

-- 6 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (6, '9788437604947', 'La casa de los espíritus', 'Debolsillo', 'Una obra que recorre las vidas de varias generaciones de la familia Trueba, desde lo fantástico hasta lo histórico.', 'FICTION', 2, 500, 448, 'SPANISH', 'MEDIUM', 1982, FALSE, TRUE, NULL);

-- 7 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (7, '9788420471839', 'Crónica de una muerte anunciada', 'Debolsillo', 'Un relato corto sobre la muerte premeditada de Santiago Nasar, basado en hechos reales.', 'MISTERY', 1, 300, 128, 'SPANISH', 'SMALL', 1981, TRUE, FALSE, NULL);

-- 8 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (8, '9788497594319', 'Los detectives salvajes', 'Anagrama', 'Una novela sobre la vida bohemia de poetas en el México de los años 70.', 'MISTERY', 1, 700, 609, 'SPANISH', 'LARGE', 1998, FALSE, TRUE, NULL);
    
-- 9 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (9, '9788423353248', 'Patria', 'Tusquets', 'Una novela que explora las vidas de dos familias vascas en el contexto del conflicto de ETA.', 'THRILLER', 1, 800, 648, 'SPANISH', 'LARGE', 2016, FALSE, TRUE, NULL);

-- 10 --
        INSERT INTO book_model (bookModelId, isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES (10, '9788445002132', 'La ciudad y los perros', 'Alfaguara', 'Una obra clave de la literatura latinoamericana que sigue a los estudiantes de un internado militar en el Perú.', 'FICTION', 2, 500, 384, 'SPANISH', 'MEDIUM', 1962, FALSE, TRUE, NULL);

-- Insertar autores --------------------------------------------------------------------------------------------------------
    
        INSERT INTO author (authorId, authorName)
        VALUES (1, 'Gabriel García Márquez');

        INSERT INTO author (authorId, authorName)
        VALUES (2, 'Miguel de Cervantes');

        INSERT INTO author (authorId, authorName)
        VALUES (3, 'Carlos Ruiz Zafón');

        INSERT INTO author (authorId, authorName)
        VALUES (4, 'Paulo Coelho');

        INSERT INTO author (authorId, authorName)
        VALUES (5, 'Isabel Allende');

        INSERT INTO author (authorId, authorName)
        VALUES (6, 'Roberto Bolaño');

        INSERT INTO author (authorId, authorName)
        VALUES (7, 'Fernando Aramburu');

        INSERT INTO author (authorId, authorName)
        VALUES (8, 'Mario Vargas Llosa');
        
-- Insertar ubicaciones ----------------------------------------------------------------------------------------------------

        INSERT INTO location (locationId, locationString)
        VALUES (1, 'Zona Norte');

        INSERT INTO location (locationId, locationString)
        VALUES (2, 'Zona Sur');

        INSERT INTO location (locationId, locationString)
        VALUES (3, 'CABA');
        
        INSERT INTO user_location (userLocationId, userId, locationId)
        VALUES (1, 1, 1)
        
        INSERT INTO user_location (userLocationId, userId, locationId)
        VALUES (2, 1, 2)
        
        INSERT INTO user_location (userLocationId, userId, locationId)
        VALUES (3, 1, 3)
                
        INSERT INTO user_location (userLocationId, userId, locationId)
        VALUES (4, 2, 1)
        
        INSERT INTO user_location (userLocationId, userId, locationId)
        VALUES (5, 3, 2)
        
        INSERT INTO user_location (userLocationId, userId, locationId)
        VALUES (6, 4, 3)
        
-- AUTORES -----------------------------------------------------------------------------------------------------------------

-- 1 -- Cien años de soledad

    INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Gabriel García Márquez'
		WHERE isbn = '9788497592208'
		
    INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Miguel de Cervantes' -- Solo Márquez es el autor, agregado para probar lista --
		WHERE isbn = '9788497592208'
		
-- 2 -- Don Quijote de la Mancha

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Miguel de Cervantes'
		WHERE isbn = '9788491050568'

-- 3 -- La sombra del viento

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Carlos Ruiz Zafón'
		WHERE isbn = '9788408186206'

-- 4 -- El amor en los tiempos del cólera

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Gabriel García Márquez'
		WHERE isbn = '9788497592451';
		
-- 5 -- El alquimista

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Paulo Coelho'
		WHERE isbn = '9780062315007';

-- 6 -- La casa de los espíritus

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Isabel Allende'
		WHERE isbn = '9788437604947';

-- 7 -- Crónica de una muerte anunciada

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Gabriel García Márquez'
		WHERE isbn = '9788420471839';

-- 8 -- Los detectives salvajes

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Roberto Bolaño'
		WHERE isbn = '9788497594319';

-- 9 -- Patria

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Fernando Aramburu'
		WHERE isbn = '9788423353248';
        
-- 10 -- La ciudad y los perros

	INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Mario Vargas Llosa'
		WHERE isbn = '9788445002132';

-- IMAGENES ----------------------------------------------------------------------------------------------------------------
		
--		INSERT INTO image (imageId, image)
--		VALUES (1, CAST('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' AS varbinary));
		
--		INSERT INTO image (imageId, image)
--		VALUES (2, CAST('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' AS varbinary));

-- LIBROS ------------------------------------------------------------------------------------------------------------------

-- Insertar Libros para Ivan ---------------------------------------------

-- 1 -- Cien años de soledad

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (1, 1, 1, 1, true, 'WORN');
	
-- 2 -- Don Quijote de la Mancha

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (2, 2, 1, 0, true, 'ACCEPTABLE');
	
-- 3 -- La sombra del viento

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (3, 3, 1, 0, false, 'GOOD');
	
-- Insertar Libros para Juli ---------------------------------------------

-- 4 -- El amor en los tiempos del cólera

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (4, 4, 2, 0, true, 'VERY_GOOD');
	
-- 5 -- El alquimista

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (5, 5, 2, 0, false, 'LIKE_NEW');
	
-- Insertar Libros para Maggie -------------------------------------------

-- 6 -- La casa de los espíritus

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (6, 6, 3, 0, true, 'NEW');
	
-- 7 -- Crónica de una muerte anunciada

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (7, 7, 3, 0, false, 'VERY_GOOD');
	
-- Insertar Libros para Tomi ---------------------------------------------

-- 8 -- Los detectives salvajes

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (8, 8, 4, 0, true, 'LIKE_NEW');
	
-- 9 -- Patria

	INSERT INTO book (bookId, bookModelId, ownerId, exchangesQty, available, bookState)
	VALUES (9, 9, 4, 0, false, 'NEW');
	
-- 10. Último libro 'La ciudad y los perros' no se inserta, queda solo como Modelo.

-- PUBLICACIONES -----------------------------------------------------------------------------------------------------------

-- De Ivan |1| -----------------------------------------------------------
	
-- 1° (Cien años de soledad |1|) --
	INSERT INTO publication (publicationId, bookId, userId, publicationState, publicationDatetime, locationId)
		SELECT 1, book.bookId, owner.userId, 'CURRENT', '2024-09-15 10:00:00', locationId
		FROM book AS book JOIN users AS owner ON mail LIKE 'modzomek@itba.edu.ar'
			JOIN location ON locationString LIKE 'Zona Sur' -- |2|
			WHERE book.bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788497592208');

-- 2° (Don Quijote de la Mancha |2|) --
	INSERT INTO publication (publicationId, bookId, userId, publicationState, publicationDatetime, locationId)
		SELECT 2, book.bookId, owner.userId, 'CURRENT', '2024-09-15 12:05:00', locationId
		FROM book AS book JOIN users AS owner ON mail LIKE 'modzomek@itba.edu.ar'
			JOIN location ON locationString LIKE 'Zona Sur' -- |2|
			WHERE book.bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788491050568');
			
-- De Juli |2| -----------------------------------------------------------

-- 3° (El amor en los tiempos del cólera |4|) --
	INSERT INTO publication (publicationId, bookId, userId, publicationState, publicationDatetime, locationId)
		SELECT 3, book.bookId, owner.userId, 'CURRENT', '2024-09-12 18:30:00', locationId
		FROM book AS book JOIN users AS owner ON mail LIKE 'jtechenski@itba.edu.ar'
			JOIN location ON locationString LIKE 'Zona Norte' -- |1|
			WHERE book.bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788497592451');

-- 4° (El alquimista |5|) --
	INSERT INTO publication (publicationId, bookId, userId, publicationState, publicationDatetime, locationId)
		SELECT 4, book.bookId, owner.userId, 'CURRENT', '2024-09-13 09:00:00', locationId
		FROM book AS book JOIN users AS owner ON mail LIKE 'jtechenski@itba.edu.ar'
			JOIN location ON locationString LIKE 'Zona Norte' -- |1|
			WHERE book.bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9780062315007');
			
-- De Maggie |3| ---------------------------------------------------------

-- 5° (La casa de los espíritus |6|) --
	INSERT INTO publication (publicationId, bookId, userId, publicationState, publicationDatetime, locationId)
		SELECT 5, book.bookId, owner.userId, 'CURRENT', '2024-09-11 14:45:00', locationId
		FROM book AS book JOIN users AS owner ON mail LIKE 'mtaurian@itba.edu.ar'
			JOIN location ON locationString LIKE 'Zona Norte' -- |1|
			WHERE book.bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788437604947');
			
-- De Tomi |4| -----------------------------------------------------------

-- 6° (Patria |8|) --
	INSERT INTO publication (publicationId, bookId, userId, publicationState, publicationDatetime, locationId)
		SELECT 6, book.bookId, owner.userId, 'CURRENT', '2024-09-13 12:15:00', locationId
		FROM book AS book JOIN users AS owner ON mail LIKE 'tscheffer@itba.edu.ar'
			JOIN location ON locationString LIKE 'Zona Norte' -- |1|
			WHERE book.bookModelId = (SELECT bookModelId FROM book_model WHERE isbn = '9788423353248');
	