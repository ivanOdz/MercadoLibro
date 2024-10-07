
-- Insertar Usuarios ---------------------------------------------------------------------------------------------------
        
        INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
        VALUES ('ivan', 'modzomek@itba.edu.ar', '', NULL, NULL, TRUE);


        INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
        VALUES ('juli', 'jtechenski@itba.edu.ar', '', NULL, NULL, TRUE);

        INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
        VALUES ('maggie', 'mtaurian@itba.edu.ar', '', NULL, NULL, TRUE);

        INSERT INTO users (userName, mail, password, imageId, verificationCode, isVerified)
        VALUES ('tomas', 'tscheffer@itba.edu.ar', '', NULL, NULL, TRUE);

-- Insertar Modelo de Libro ---------------------------------------------------------------------------------------------------

-- 1 --
        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788497592208', 'Cien años de soledad', 'Sudamericana', 'Una novela emblemática del realismo mágico que narra la historia de la familia Buendía a lo largo de varias generaciones.', 2, 1, 600, 471, 1, 1, 1967, FALSE, TRUE, NULL);
    
-- 2 --        
        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788491050568', 'Don Quijote de la Mancha', 'Real Academia Española', 'La obra maestra de la literatura española que sigue las aventuras del caballero Don Quijote y su escudero Sancho Panza.', 3, 1, 800, 1000, 1, 2, 1605, TRUE, FALSE, NULL);

-- 3 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788408186206', 'La sombra del viento', 'Planeta', 'Una novela fascinante ambientada en la Barcelona de la posguerra que mezcla misterio, amor y tragedia.', 4, 2, 400, 575, 1, 1, 2001, TRUE, FALSE, NULL);

-- 4 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788497592451', 'El amor en los tiempos del cólera', 'Sudamericana', 'Una historia de amor que se extiende a lo largo de más de 50 años, escrita por el ganador del Premio Nobel.', 5, 1, 500, 490, 1, 1, 1985, FALSE, TRUE, NULL);

-- 5 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9780062315007', 'El alquimista', 'HarperCollins', 'Una novela filosófica sobre un joven pastor que sigue su sueño de encontrar un tesoro en Egipto.', 6, 3, 300, 208, 1, 1, 1988, TRUE, FALSE, NULL);

-- 6 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788437604947', 'La casa de los espíritus', 'Debolsillo', 'Una obra que recorre las vidas de varias generaciones de la familia Trueba, desde lo fantástico hasta lo histórico.', 7, 2, 500, 448, 1, 1, 1982, FALSE, TRUE, NULL);

-- 7 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788420471839', 'Crónica de una muerte anunciada', 'Debolsillo', 'Un relato corto sobre la muerte premeditada de Santiago Nasar, basado en hechos reales.', 2, 1, 300, 128, 1, 1, 1981, TRUE, FALSE, NULL);

-- 8 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788497594319', 'Los detectives salvajes', 'Anagrama', 'Una novela sobre la vida bohemia de poetas en el México de los años 70.', 8, 1, 700, 609, 1, 1, 1998, FALSE, TRUE, NULL);
    
-- 9 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788423353248', 'Patria', 'Tusquets', 'Una novela que explora las vidas de dos familias vascas en el contexto del conflicto de ETA.', 9, 1, 800, 648, 1, 1, 2016, FALSE, TRUE, NULL);

-- 10 --

        INSERT INTO book_model (isbn, title, editorial, description, genre, edition, weight, pages, bookLanguage, dimension, publicationYear, isPocketEdition, isHardcover, imageId)
        VALUES ('9788445002132', 'La ciudad y los perros', 'Alfaguara', 'Una obra clave de la literatura latinoamericana que sigue a los estudiantes de un internado militar en el Perú.', 3, 2, 500, 384, 1, 1, 1962, FALSE, TRUE, NULL);

-- Insertar autores ---------------------------------------------------------------------------------------------------
    

        INSERT INTO author (authorName)
        VALUES ('Gabriel García Márquez');

        INSERT INTO author (authorName)
        VALUES ('Miguel de Cervantes');

        INSERT INTO author (authorName)
        VALUES ('Carlos Ruiz Zafón');

        INSERT INTO author (authorName)
        VALUES ('Paulo Coelho');

        INSERT INTO author (authorName)
        VALUES ('Isabel Allende');

        INSERT INTO author (authorName)
        VALUES ('Roberto Bolaño');

        INSERT INTO author (authorName)
        VALUES ('Fernando Aramburu');

        INSERT INTO author (authorName)
        VALUES ('Mario Vargas Llosa');

        
-- Insertar ubicación ---------------------------------------------------------------------------------------------------

        INSERT INTO location (locationString)
        VALUES ('Zona Norte');

        INSERT INTO location (locationString)
        VALUES ('Zona Sur');
    

-- AUTORES ------------------------------------------------------------------------------------------------------------------

-- 1 -- Cien años de soledad

    INSERT INTO book_author (bookModelId, authorId)
		SELECT bookModelId, authorId
		FROM book_model
		JOIN author ON authorName = 'Gabriel García Márquez'
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

-- LIBROS + REVIEWS ----------------------------------------------------------------------------------------------------------------
        
-- Insertar Libros para Ivan --

-- 1° -- La ciudad y los perros

	INSERT INTO book (bookModelId, ownerId, bookState, exchangesQty)
		SELECT bookModelId, userId, 1, 0
		FROM book_model AS bookModel
		JOIN users ON mail LIKE 'modzomek@itba.edu.ar'
		WHERE isbn = '9788445002132' AND NOT EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId)
		
	INSERT INTO book_rating (userId, bookModelId, rating)
		SELECT userId, bookModel.bookModelId, 3
		FROM book_model AS bookModel
		JOIN users ON mail = 'modzomek@itba.edu.ar'
		WHERE isbn = '9788445002132' AND EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId);
		 
-- 2° -- Patria

	INSERT INTO book (bookModelId, ownerId, bookState, exchangesQty)
		SELECT bookModelId, userId, 1, 0
		FROM book_model AS bookModel
		JOIN users ON mail LIKE 'modzomek@itba.edu.ar'
		WHERE isbn = '9788423353248' AND NOT EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId)
		
	INSERT INTO book_rating (userId, bookModelId, rating)
		SELECT userId, bookModel.bookModelId, 2
		FROM book_model AS bookModel
		JOIN users ON mail = 'modzomek@itba.edu.ar'
		WHERE isbn = '9788423353248' AND EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId);

-- Insertar Libros para Juli

--  1° -- La casa de los espíritus

	INSERT INTO book (bookModelId, ownerId, bookState, exchangesQty)
		SELECT bookModelId, userId, 1, 0
		FROM book_model AS bookModel
		JOIN users ON mail LIKE 'jtechenski@itba.edu.ar'
		WHERE isbn = '9788437604947' AND NOT EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId)
		
	INSERT INTO book_rating (userId, bookModelId, rating)
		SELECT userId, bookModel.bookModelId, 3
		FROM book_model AS bookModel
		JOIN users ON mail = 'jtechenski@itba.edu.ar'
		WHERE isbn = '9788437604947' AND EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId);
        
-- Insertar Libros para Maggie --

-- 1° -- El amor en los tiempos del cólera

	INSERT INTO book (bookModelId, ownerId, bookState, exchangesQty)
		SELECT bookModelId, userId, 2, 0
		FROM book_model AS bookModel
		JOIN users ON mail LIKE 'mtaurian@itba.edu.ar'
		WHERE isbn = '9788497592451' AND NOT EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId)
		
	INSERT INTO book_rating (userId, bookModelId, rating)
		SELECT userId, bookModel.bookModelId, 4
		FROM book_model AS bookModel
		JOIN users ON mail = 'mtaurian@itba.edu.ar'
		WHERE isbn = '9788497592451' AND EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId);
		
-- Insertar Libros para Tom's --

-- 1° -- Don Quijote de la Mancha
		
	INSERT INTO book (bookModelId, ownerId, bookState, exchangesQty)
		SELECT bookModelId, userId, 2, 0
		FROM book_model AS bookModel
		JOIN users ON mail LIKE 'tscheffer@itba.edu.ar'
		WHERE isbn = '9788491050568' AND NOT EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId)
		
	INSERT INTO book_rating (userId, bookModelId, rating)
		SELECT userId, bookModel.bookModelId, 5
		FROM book_model AS bookModel
		JOIN users ON mail = 'tscheffer@itba.edu.ar'
		WHERE isbn = '9788491050568' AND EXISTS (SELECT 1 FROM book WHERE bookModelId = bookModel.bookModelId);
        