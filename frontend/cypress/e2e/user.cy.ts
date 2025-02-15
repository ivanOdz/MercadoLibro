describe('Register Tests', () => {
    beforeEach(() => {
        cy.visit('/auth/register');
    });

    // WORKING
    it('should register successfully', () => {
        cy.intercept('POST', '**/users', (req) => {
            req.reply({
                statusCode: 201,
                headers: {
                    Location: '/users/123'
                },
            });
        }).as('registerRequest');

        cy.get('[data-cy=email]').type('testuser@example.com');
        cy.get('[data-cy=username]').type('testuser');
        cy.get('[data-cy=password]').type('password123');
        cy.get('[data-cy=repeat-password]').type('password123');
        cy.get('[data-cy=submit]').click();

        cy.wait('@registerRequest');

        cy.url().should('include', '/auth/register/success');
    });
});


describe('User Verification Page', () => {

    // WORKING
    it('should show success message on successful verification', () => {
        cy.intercept('POST', '**/users', { statusCode: 204 }).as('verifyRequest');

        cy.visit('/auth/verify?verification_code=ewN9fOewnoeM4e8mfpNqwo');

        cy.wait('@verifyRequest');

        cy.wait(1000);

        cy.get('[data-cy=title-success]').should('be.visible');

        cy.get('button').get('[data-cy=button-home]').click();
        cy.url().should('eq', Cypress.config().baseUrl + '/publications');
    });

    // WORKING
    it('should show error message on verification failure', () => {
        cy.intercept('POST', '**/users', { statusCode: 400, body: { message: 'Código inválido' } }).as('verifyRequest');

        cy.visit('/auth/verify?verification_code=ewN9fOewnoeM4e8mfpNqwo');

        cy.wait('@verifyRequest');

        cy.wait(1000);

        cy.get('[data-cy=title-error]').should('be.visible');
    });
});
