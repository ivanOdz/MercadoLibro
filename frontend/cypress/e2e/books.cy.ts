describe("Books tests", () => {
    beforeEach(() => {
        cy.login("testuser", "password", true);

        cy.interceptBooksRequests()
    })

    it("should load general books", () => {
        cy.visit("/books")
        cy.waitBooksRequests()

        cy.get('[data-cy=books]').should("have.length.greaterThan", 0)

    })

    it("should load my books in a publication", () => {
        cy.intercept('GET', '**/users/*', { fixture: 'other-user.json' }).as('getPublicationUser');
        cy.intercept('GET', '**/publications/*', { fixture: 'publication.json' }).as('getPublication');
        cy.interceptPublicationRequests()

        cy.get('[data-cy=publications]').first().click();

        cy.waitPublicationRequests();
        cy.wait('@getPublicationUser');

        cy.get('[data-cy=exchange-button]').click();

        cy.waitBooksRequests()

        cy.get('.book').should('have.length.greaterThan', 0);
    })

    it("should create a book from preloaded", () => {
        cy.intercept('GET', '**/book_models?*', { fixture: 'book_models.json' }).as('getBookModels');

        cy.visit("/books/library");

        cy.intercept('POST', '**/books', { statusCode: 200 }).as('createBook');

        cy.wait('@getBookModels');

        cy.get('[data-cy=book_models]').first().click();

        cy.get('.star-rating .star-icon').eq(3).click();

        cy.get('select').select('good');

        cy.get('[data-cy=submit]').click();

        cy.wait('@createBook').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });
    });


    it("should edit a book's condition", () => {
        cy.intercept('PATCH', '**/books/*', { statusCode: 200 }).as('editBook');

        cy.visit("/books");

        cy.waitBooksRequests();

        cy.get('[data-cy=edit]').first().click();

        cy.get('select').select('good');

        cy.get('[data-cy=submit-edit]').click();

        cy.wait('@editBook').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        })
    })

    it("should create a book from scratch", () => {
        cy.intercept('POST', '**/books', { statusCode: 200 }).as('createBook');
        cy.intercept('POST', '**/book_models', { statusCode: 200 });

        cy.visit("/books/add");

        cy.get('input').eq(0).type('Test book');

        cy.get('input').eq(1).type('Test author');

        cy.get('input').eq(2).clear().type('2021');

        cy.get('input').eq(3).type('9780525562443');

        cy.get('input').eq(7).type('Test publisher');


        cy.get('select').eq(0).select('fantasy');
        cy.get('select').eq(3).select('good');


        cy.get('[data-cy=submit]').click();

        cy.wait('@createBook').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });
    })

})