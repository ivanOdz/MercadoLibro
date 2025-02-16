describe('General exchanges tests', () => {
    beforeEach(() => {
        cy.login('testuser', 'password', false);



        cy.visit('/publication/3');
    });

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
        cy.intercept('GET', '**/exchanges/*/messages', { fixture: 'messages.json' }).as('getMessages');

        cy.intercept('PATCH', '**/exchanges/*', { statusCode: 200 }).as('confirmExchange');
        cy.intercept('POST', '**/exchanges/*/messages', { fixture: 'message.json' }).as('sendMessage');

        cy.visit('/exchanges');
    });

    it('should load active exchanges', () => {
        cy.wait('@getExchanges');
        cy.waitExchangesRequests();

        cy.get('[data-cy=exchanges]').should('have.length.greaterThan', 0);
    });

    it('should confirm an exchange correctly', () => {
        cy.wait('@getExchanges');
        cy.waitExchangesRequests();


        // open modal
        cy.get('.confirm-button').first().click();

        // confirm exchange
        cy.get('[data-cy=confirm-button]').click();

        cy.wait('@confirmExchange').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });


    it('should send a message in an exchange', () => {
        cy.wait('@getExchanges');
        cy.waitExchangesRequests();

        cy.get('.card').first().click();

        cy.get('[data-cy=open-chat]').click();
        cy.get('[data-cy=type-message]').type('Hola, ¿cómo coordinamos el intercambio?', {force: true});

        cy.get('[data-cy=send-message]').click();

        cy.wait('@sendMessage').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });

    it('should get messages correctly', () => {
        cy.wait('@getExchanges');
        cy.waitExchangesRequests();

        cy.get('.card').first().click();

        cy.get('[data-cy=open-chat]').click();
        cy.wait('@getMessages').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });

});




describe('Requests Exchanges tests', () => {
    beforeEach(() => {
        cy.login('testuser', 'password', true);

        cy.intercept('GET', '**/exchanges?*is_requester=true&is_offerer=false*', {fixture: 'requested-exchanges.json' }).as('getSolicitedExchanges');
        cy.intercept('GET', '**/exchanges?*is_requester=false&is_offerer=true*', { fixture: 'offered-exchanges.json' }).as('getOfferedExchanges');

        cy.intercept('GET', '**/publications/*', { fixture: 'publication.json' }).as('getPublication');
        cy.intercept('GET', '**/users/*', { fixture: 'user.json' }).as('getUser');
        cy.intercept('GET', '**/books/*', { fixture: 'book.json' }).as('getBook');
        cy.intercept('GET', '**/book_models/*', { fixture: 'book_model.json' }).as('getBookModel');
        cy.intercept('GET', '**/users/*/locations?*', { fixture: 'locations.json' }).as('getLocations');
        cy.intercept('GET', '**/exchanges/*/messages', { fixture: 'messages.json' }).as('getMessages');

        cy.intercept('PATCH', '**/exchanges/*?*accepted=true', { statusCode: 200 }).as('acceptExchange');
        cy.intercept('PATCH', '**/exchanges/*?*accepted=false', { statusCode: 200 }).as('rejectExchange');

        cy.visit('/exchanges/requests');
    });

    it('should load solicited exchanges', () => {
        cy.wait('@getSolicitedExchanges');
        cy.waitExchangesRequests();

        // TODO html
        cy.get('[data-cy=solicited-exchanges]').should('have.length.greaterThan', 0);
    });

    it('should load offered exchanges', () => {
        cy.wait('@getOfferedExchanges');
        cy.waitExchangesRequests();

        // TODO html
        cy.get('[data-cy=offered-exchanges]').should('have.length.greaterThan', 0);
    });

    it('should accept an exchange correctly', () => {
        cy.wait('@getOfferedExchanges');
        cy.waitExchangesRequests();

        //
        // // open modal
        // cy.get('.confirm-button').first().click();
        //
        // // confirm exchange
        // cy.get('[data-cy=confirm-button]').click();

        cy.wait('@acceptExchange').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });

     it('should reject an exchange correctly', () => {
            cy.wait('@getOfferedExchanges');
            cy.waitExchangesRequests();

            // // open modal
            // cy.get('.confirm-button').first().click();
            //
            // // confirm exchange
            // cy.get('[data-cy=confirm-button]').click();

            cy.wait('@rejectExchange').then(({ response }) => {
                expect(response?.statusCode).to.eq(200);
            });

        });

});




describe('History Exchanges tests', () => {
    beforeEach(() => {
        cy.login('testuser', 'password', true);

        cy.intercept('GET', '**/exchanges?*state=TERMINATED*', { fixture: 'completed-exchanges.json' }).as('getCompletedExchanges');
        cy.intercept('GET', '**/exchanges?*state=REJECTED*', { fixture: 'rejected-exchanges.json' }).as('getRejectedExchanges');

        cy.intercept('GET', '**/publications/*', { fixture: 'publication.json' }).as('getPublication');
        cy.intercept('GET', '**/users/*', { fixture: 'user.json' }).as('getUser');
        cy.intercept('GET', '**/books/*', { fixture: 'book.json' }).as('getBook');
        cy.intercept('GET', '**/book_models/*', { fixture: 'book_model.json' }).as('getBookModel');
        cy.intercept('GET', '**/users/*/locations?*', { fixture: 'locations.json' }).as('getLocations');
        cy.intercept('GET', '**/exchanges/*/messages', { fixture: 'messages.json' }).as('getMessages');


        cy.visit('/exchanges/history');
    });

    it('should load completed exchanges', () => {
        cy.wait('@getCompletedExchanges');
        cy.waitExchangesRequests();

        cy.get('[data-cy=exchanges]').should('have.length.greaterThan', 0);
    });

    it('should load rejected exchanges', () => {
        cy.wait('@getRejectedExchanges');
        cy.waitExchangesRequests();

        cy.get('[data-cy=exchanges]').should('have.length.greaterThan', 0);
    });

    it('should confirm an exchange correctly', () => {
        cy.waitExchangesRequests();


        // open modal
        cy.get('.confirm-button').first().click();

        // confirm exchange
        cy.get('[data-cy=confirm-button]').click();

        cy.wait('@confirmExchange').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });


    it('should create a review in an exchange', () => {
        cy.waitExchangesRequests();

        cy.get('.card').first().click();

        cy.get('[data-cy=open-chat]').click();
        cy.get('[data-cy=type-message]').type('Hola, ¿cómo coordinamos el intercambio?', {force: true});

        cy.get('[data-cy=send-message]').click();

        cy.wait('@sendMessage').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });


});
