/// <reference types="cypress" />
// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
//
// declare global {
//   namespace Cypress {
//     interface Chainable {
//       login(email: string, password: string): Chainable<void>
//       drag(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       dismiss(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       visit(originalFn: CommandOriginalFn, url: string, options: Partial<VisitOptions>): Chainable<Element>
//     }
//   }
// }


Cypress.Commands.add('login', (username: string, password: string, rememberMe: boolean = false) => {
    cy.visit('/auth/login');

    cy.intercept('HEAD', '**/book_models', (req) => {
        req.reply({
            statusCode: 200,
            headers: {
                'X-Access-Token': 'fake_access_token',
                'X-Refresh-Token': 'fake_refresh_token',
                'X-User-URI': '/users/1'
            }
        });
    }).as('loginRequest');

    cy.get('[data-cy=username]').type(username)
    cy.get('[data-cy=password]').type(password);
    if (rememberMe) {
        cy.get('[data-cy=remember-me]').click();
    }
    cy.get('[data-cy=submit]').click();
});

Cypress.Commands.add('waitExchangesRequests', () => {
    cy.wait('@getPublication');
    cy.wait('@getUser');
    cy.wait('@getBook');
    cy.wait('@getBookModel');
    cy.wait('@getLocations');
    cy.wait('@getMessages');
});


Cypress.Commands.add('interceptExchangesRequests', () => {
    cy.intercept('GET', '**/publications/*', { fixture: 'publication.json' }).as('getPublication');
    cy.intercept('GET', '**/users/*', { fixture: 'user.json' }).as('getUser');
    cy.intercept('GET', '**/books/*', { fixture: 'book.json' }).as('getBook');
    cy.intercept('GET', '**/book_models/*', { fixture: 'book_model.json' }).as('getBookModel');
    cy.intercept('GET', '**/users/*/locations?*', { fixture: 'locations.json' }).as('getLocations');
    cy.intercept('GET', '**/exchanges/*/messages', { fixture: 'messages.json' }).as('getMessages');
});


Cypress.Commands.add('interceptPublicationRequests', () => {
    cy.intercept('GET', '**/users/*', { fixture: 'user.json' }).as('getUser');
    cy.intercept('GET', '**/users/*', { fixture: 'other-user.json' }).as('getPublicationUser');
    cy.intercept('GET', '**/publications/*', { fixture: 'publication.json' }).as('getPublication');
    cy.intercept('GET', '**/publications/*', { fixture: 'my-publication.json' }).as('getMyPublication');
    cy.intercept('GET', '**/books/*', { fixture: 'book.json' }).as('getBook');
    cy.intercept('GET', '**/books?*available=true*', { fixture: 'available-books.json' }).as('getBooks');
    cy.intercept('GET', '**/book_models/*', { fixture: 'book_model.json' }).as('getBookModel');
    cy.intercept('GET', '**/users/*/locations?*', { fixture: 'locations.json' }).as('getPublicationsLocations');
    cy.intercept('GET', '**/users/*/locations', { fixture: 'new-locations.json' }).as('getUserLocations');
});

Cypress.Commands.add('waitPublicationRequests', () => {
    cy.wait('@getBook');
    cy.wait('@getPublicationsLocations');
    cy.wait('@getBookModel');
});

Cypress.Commands.add('interceptPublicationsRequests', () => {
    cy.intercept('GET', /\/api\/publications(\?.*)?/, { fixture: 'publications.json' }).as('getPublications');
    cy.intercept('GET', /\/api\/publications\?.*user_id=[^&]+.*/, { fixture: 'my-publications.json' }).as('getMyPublications');
    cy.intercept('GET', /\/api\/publications\?.*(favorites=true).*(user_id=[^&]+)|.*(user_id=[^&]+).*(favorites=true).*/, { fixture: 'favorite-publications.json' }).as('getFavoritePublications');


    cy.intercept('GET', '**/users/*', { fixture: 'user.json' }).as('getUser');
    cy.intercept('GET', '**/books/*', { fixture: 'book.json' }).as('getBook');
    cy.intercept('GET', '**/book_models/*', { fixture: 'book_model.json' }).as('getBookModel');
    cy.intercept('GET', '**/users/*/locations?*', { fixture: 'locations.json' }).as('getLocations');
});

Cypress.Commands.add('waitPublicationsRequests', () => {
    cy.wait('@getUser');
    cy.wait('@getBook');
    cy.wait('@getBookModel');
    cy.wait('@getLocations');
})

Cypress.Commands.add('interceptBooksRequests', () => {
    cy.intercept('GET', '**/books?*', { fixture: 'available-books.json' }).as('getBooks');
    cy.intercept('GET', '**/book_models/*', { fixture: 'book_model.json' }).as('getBookModel');
})

Cypress.Commands.add('waitBooksRequests', () => {
    cy.wait('@getBooks');
    cy.wait('@getBookModel');
})


declare namespace Cypress {
    interface Chainable {
        login(username: string, password: string, rememberMe?: boolean): Chainable<void>;
        waitExchangesRequests(): Chainable<void>;
        interceptExchangesRequests(): Chainable<void>;
        interceptPublicationRequests(): Chainable<void>;
        waitPublicationRequests(): Chainable<void>;
        interceptPublicationsRequests(): Chainable<void>;
        waitPublicationsRequests(): Chainable<void>;
        interceptBooksRequests(): Chainable<void>;
        waitBooksRequests(): Chainable<void>;
    }
}
