describe("Publications tests", () => {
    beforeEach(() => {
        cy.interceptPublicationsRequests()
    })

    // it("should load general publications logged in", () => {
    //     cy.login("testuser", "password123", true)
    //     cy.visit("/publications")
    //     cy.wait('@getPublications');
    //     cy.waitPublicationsRequests()
    //
    //     cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    // })
    //
    // it("should load general publications", () => {
    //     cy.visit("/publications")
    //     cy.wait('@getPublications');
    //     cy.waitPublicationsRequests()
    //
    //     cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    // })
    //
    // it("should load my publications", () => {
    //     cy.login("testuser", "password123", true)
    //     cy.visit("/publications/mine")
    //     cy.wait('@getMyPublications');
    //     cy.waitPublicationsRequests()
    //
    //     cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    // })
    //
    // it("should load favorite publications", () => {
    //     cy.login("testuser", "password123", true)
    //     cy.visit("/publications/favorites")
    //     cy.wait('@getFavoritePublications');
    //     cy.waitPublicationsRequests()
    //
    //     cy.get('[data-cy=publications]').should('have.length.greaterThan', 0);
    // })
    //
    // it("should create a publications", () => {
    //     cy.login("testuser", "password123", true)
    //     cy.interceptBooksRequests()
    //     cy.intercept('POST', '**/publications', { fixture: 'publication.json' }).as('createPublication')
    //
    //     cy.visit("/books")
    //
    //     cy.waitBooksRequests()
    //
    //     cy.get('[data-cy=books]').first().click()
    //
    //     cy.get('[data-cy=locations-select]').click()
    //     cy.get('.p-select-option').first().click()
    //
    //     cy.get('[data-cy=submit]').click()
    //
    //     cy.wait('@createPublication').then(({ response }) => {
    //         expect(response?.statusCode).to.eq(200);
    //     });
    //
    // })

    it("should edit a publication's locations", () => {
        cy.login('testuser', 'password123', true);

        cy.intercept('PATCH', '**/publications/*').as('updatePublication');

        cy.wait('@getPublications');

        cy.login('testuser', 'password3453', false);
        cy.get('[data-cy=publications]').first().click();

        cy.wait('@getUser');
        cy.wait('@getMyPublication')
        cy.wait('@getUser');
        cy.waitPublicationRequests();

        cy.get('[data-cy=edit-locations]').click();

        cy.get('[data-cy=locations-select]').click();
        cy.get('.p-select-option').first().click();

        cy.get('[data-cy=submit]').click({ force: true });

        cy.wait('@updatePublication').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });
    })

    // IMPLEMENT
    it("should delete a publication", () => {

    })

})