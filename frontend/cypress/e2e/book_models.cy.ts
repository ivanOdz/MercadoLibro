describe("Book models tests", () => {
    beforeEach(() => {
        cy.login("testuser", "password", true);
        cy.intercept('GET', '**/book_models?*', { fixture: 'book_models.json' }).as('getBookModels');
    })


    it("should load general book models", () => {
        cy.visit("/books/library");

        cy.wait('@getBookModels');

        cy.get('[data-cy=book_models]').should("have.length.greaterThan", 0);
    })

    it("should create a book model", () => {
        cy.intercept('POST', '**/book_models', { statusCode: 200 }).as('createBookModel');

        cy.visit("/books/add");

        cy.get('input').eq(0).type('Test book');

        cy.get('input').eq(1).type('Test author');

        cy.get('input').eq(2).clear().type('2021');

        cy.get('input').eq(3).type('9780525562443');

        cy.get('input').eq(7).type('Test publisher');


        cy.get('select').eq(0).select('fantasy');
        cy.get('select').eq(3).select('good');


        cy.get('[data-cy=submit]').click();

        cy.wait('@createBookModel').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });
    })
})