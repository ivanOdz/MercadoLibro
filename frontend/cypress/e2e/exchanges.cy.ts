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

        cy.interceptExchangesRequests();

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

        cy.intercept('GET', '**/exchanges?user_id=*&state=PENDING&is_offerer=false&is_requester=true&page=*', {
            fixture: 'requested-exchanges.json'
        }).as('getSolicitedExchanges');

        cy.intercept('GET', '**/exchanges?user_id=*&state=PENDING&is_offerer=true&is_requester=false&page=*', {
            fixture: 'offered-exchanges.json'
        }).as('getOfferedExchanges');



        cy.interceptExchangesRequests();

        cy.intercept('PATCH', '**/exchanges/*').as('updateExchange');

        cy.visit('/exchanges/requests');
    });

    it('should load solicited exchanges', () => {

        cy.wait('@getSolicitedExchanges').its('response.statusCode').should('eq', 200);
        cy.waitExchangesRequests();

        cy.get('[data-cy="requests-panel"]').click();

        cy.get('.solicited-exchanges').should('have.length.greaterThan', 0);
    });

    it('should load offered exchanges', () => {
        cy.wait('@getOfferedExchanges');
        cy.waitExchangesRequests();

        cy.get('[data-cy=offered-exchanges]').should('have.length.greaterThan', 0);
    });

    it('should accept an exchange correctly', () => {
        cy.wait('@getOfferedExchanges');
        cy.waitExchangesRequests();

        // open modal
        cy.get('[data-cy=accept-button]').first().click();
        // accept exchange
        cy.get('[data-cy=accept]').click();

        cy.wait('@updateExchange').then((interception) => {
            expect(interception.request.body).to.deep.equal({
                acceptCode: 123,
                requester: null,
                accepted: true
            });
        });

    });

     it('should reject an exchange correctly', () => {
            cy.wait('@getOfferedExchanges');
            cy.waitExchangesRequests();

            // open modal
            cy.get('[data-cy=reject-button]').first().click();

            // reject exchange
            cy.get('[data-cy=reject]').click();
             cy.wait('@updateExchange').then((interception) => {
                 expect(interception.request.body).to.deep.equal({
                     acceptCode: 123,
                     requester: null,
                     accepted: false
                 });

            });
     });

});




describe('History Exchanges tests', () => {
    beforeEach(() => {
        cy.login('testuser', 'password', true);

        cy.intercept('GET', '**/exchanges?user_id=*&state=TERMINATED&is_offerer=true&is_requester=true&page=*', { fixture: 'completed-exchanges.json' }).as('getCompletedExchanges');
        cy.intercept('GET', '**/exchanges?user_id=*&state=REJECTED&is_offerer=true&is_requester=true&page=*', { fixture: 'rejected-exchanges.json' }).as('getRejectedExchanges');

        cy.interceptExchangesRequests();

        cy.intercept('POST', '**/users/*/reviews', { fixture: 'review.json' }).as('createReview');
        cy.visit('/exchanges/history');
    });

    it('should load completed exchanges', () => {
        cy.wait('@getCompletedExchanges');
        cy.waitExchangesRequests();

        cy.get('[data-cy=completed-exchanges]').should('have.length.greaterThan', 0);
    });

    it('should load rejected exchanges', () => {
        cy.wait('@getRejectedExchanges');
        cy.waitExchangesRequests();

        cy.get('[data-cy=reject-panel]').click();

        cy.get('[data-cy=rejected-exchanges]').should('have.length.greaterThan', 0);
    });

    it('should create a review in an exchange', () => {
        cy.waitExchangesRequests();

        cy.get('.card').first().click();

        cy.get('[data-cy=review-button]').click();

        cy.get('p-rating')
            .find('.p-rating-icon')
            .eq(4)
            .click();

        cy.get('[data-cy=review-text]').type('This is a review', { force: true } );

        cy.get('[data-cy=review-send').click()

        cy.wait('@createReview').then(({ response }) => {
            expect(response?.statusCode).to.eq(200);
        });

    });

    // IMPLEMENT: reviews are not loaded yet just can be posted
    it('should load a review correctly', () => {

    });


});
