describe('General exchanges tests', () => {

    it('should create an exchange.json', () => {

    });
})



describe('Active Exchanges tests', () => {
    beforeEach(() => {
        cy.login('testuser', 'password', true);

        cy.intercept('GET', '**/exchanges?*', { fixture: 'active-exchanges.json' }).as('getExchanges');

        cy.intercept('GET', '**/publications/*', { fixture: 'publication.json' }).as('getPublication');
        cy.intercept('GET', '**/users/*', { fixture: 'user.json' }).as('getUser');
        cy.intercept('GET', '**/books/*', { fixture: 'book.json' }).as('getBook');
        cy.intercept('GET', '**/book_models/*', { fixture: 'book_model.json' }).as('getBookModel');
        cy.intercept('GET', '**/users/*/locations?*', { fixture: 'locations.json' }).as('getLocations');
        cy.intercept('GET', '**/exchanges/*/chat', { fixture: 'messages.json' }).as('getMessages');

        cy.intercept('PATCH', '**/exchanges/*', { statusCode: 200 }).as('confirmExchange');
        cy.intercept('POST', '**/messages', { fixture: 'message.json' }).as('sendMessage');

        cy.visit('/exchanges');
    });

    it('should load active exchanges', () => {
        cy.waitExchangesRequests();

        cy.get('[data-cy=exchanges]').should('have.length.greaterThan', 0);
    });

    it('should confirm an exchange.json correctly', () => {
        cy.waitExchangesRequests();


        // open modal
        cy.get('.confirm-button').first().click();

        // confirm exchange
        cy.get('[data-cy=confirm-button]').click();

        cy.wait('@confirmExchange').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });

});

