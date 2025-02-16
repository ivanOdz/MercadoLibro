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
        cy.intercept('GET', '**/exchanges/*/messages', { fixture: 'messages.json' }).as('getMessages');

        cy.intercept('PATCH', '**/exchanges/*', { statusCode: 200 }).as('confirmExchange');
        cy.intercept('POST', '**/exchanges/*/messages', { fixture: 'message.json' }).as('sendMessage');

        cy.visit('/exchanges');
    });

    // it('should load active exchanges', () => {
    //     cy.waitExchangesRequests();
    //
    //     cy.get('[data-cy=exchanges]').should('have.length.greaterThan', 0);
    // });
    //
    // it('should confirm an exchange.json correctly', () => {
    //     cy.waitExchangesRequests();
    //
    //
    //     // open modal
    //     cy.get('.confirm-button').first().click();
    //
    //     // confirm exchange
    //     cy.get('[data-cy=confirm-button]').click();
    //
    //     cy.wait('@confirmExchange').then(({ response }) => {
    //         expect(response?.statusCode).to.eq(200);
    //     });
    //
    // });


    it('should send a message in an exchange.json', () => {
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



/////////////////////
//
//
// describe('Active exchanges tests', () => {
//     const apiUrl = 'https://api.example.com/exchanges'; // Reemplaza con tu URL real
//
//     beforeEach(() => {
//         cy.intercept('GET', `${apiUrl}*`, { fixture: 'active-exchanges.json' }).as('getExchanges');
//         cy.intercept('POST', `${apiUrl}*`, { statusCode: 201, headers: { Location: '/exchange.json/123' } }).as('createExchange');
//         cy.intercept('PATCH', `${apiUrl}*`, { statusCode: 200 }).as('updateExchange');
//     });
//
//     it('debería obtener un intercambio por URL', () => {
//         cy.request(`${apiUrl}/123`).then((response) => {
//             expect(response.status).to.eq(200);
//             expect(response.body).to.have.property('id', 123);
//         });
//     });
//
//     it('debería obtener mensajes de un intercambio', () => {
//         cy.request(`${apiUrl}/123/messages`).then((response) => {
//             expect(response.status).to.eq(200);
//             expect(response.body).to.be.an('array');
//         });
//     });
//
//     it('debería crear un intercambio correctamente', () => {
//         cy.request('POST', `${apiUrl}`, {
//             bookURN: 'urn:book:456',
//             locationURN: 'urn:location:789',
//             publicationURN: 'urn:pub:101'
//         }).then((response) => {
//             expect(response.status).to.eq(201);
//             expect(response.headers).to.have.property('location');
//         });
//     });
//
//     it('debería actualizar un intercambio', () => {
//         cy.request('PATCH', `${apiUrl}/123`, {
//             acceptCode: 200,
//             requester: true,
//             accepted: true
//         }).then((response) => {
//             expect(response.status).to.eq(200);
//         });
//     });
//
//     it('debería manejar errores en la actualización de intercambios', () => {
//         cy.intercept('PATCH', `${apiUrl}/123`, { statusCode: 400 }).as('failedUpdate');
//
//         cy.request({ method: 'PATCH', url: `${apiUrl}/123`, failOnStatusCode: false }).then((response) => {
//             expect(response.status).to.eq(400);
//         });
//     });

// });
