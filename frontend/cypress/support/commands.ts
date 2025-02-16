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

declare namespace Cypress {
    interface Chainable {
        login(username: string, password: string, rememberMe?: boolean): Chainable<void>;
        waitExchangesRequests(): Chainable<void>;
    }
}
