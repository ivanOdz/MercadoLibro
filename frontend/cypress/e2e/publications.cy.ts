describe("Publications tests", () => {
    beforeEach(() => {
        cy.interceptPublicationsRequests()
    })

    it("should load general publications logged in", () => {
        cy.login("testuser", "password123", true)
        cy.visit("/publications")
        cy.wait('@getPublications');
        cy.waitPublicationsRequests()

        cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    })

    it("should load general publications", () => {
        cy.visit("/publications")
        cy.wait('@getPublications');
        cy.waitPublicationsRequests()

        cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    })

    it("should load my publications", () => {
        cy.login("testuser", "password123", true)
        cy.visit("/publications/mine")
        cy.wait('@getMyPublications');
        cy.waitPublicationsRequests()

        cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    })

    it("should load favorite publications", () => {
        cy.login("testuser", "password123", true)
        cy.visit("/publications/favorites")
        cy.wait('@getFavoritePublications');
        cy.waitPublicationsRequests()

        cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    })

    it("should create a publications", () => {
        cy.login("testuser", "password123", true)
        cy.interceptBooksRequests()
        cy.intercept('POST', '**/publications', { fixture: 'publication.json' }).as('createPublication')

        cy.visit("/my-books")

        cy.get('[data-cy=books]').first().click()

        cy.get('[data-cy=locations-select]').click()
        cy.get('.p-select-option').first().click()

        cy.get('[data-cy=submit]').click()

        cy.wait('@createPublication').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    })

    // IMPLEMENT
    it("should edit a publication's locations", () => {

    })

    // IMPLEMENT
    it("should delete a publication", () => {

    })

})