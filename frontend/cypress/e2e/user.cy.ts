describe('Register Tests', () => {
    beforeEach(() => {
        cy.visit('/auth/register');
    });

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


describe('User Verification Tests', () => {

    it('should show success message on successful verification', () => {
        cy.intercept('POST', '**/users', { statusCode: 204 }).as('verifyRequest');

        cy.visit('/auth/verify?verification_code=ewN9fOewnoeM4e8mfpNqwo');

        cy.wait('@verifyRequest');

        cy.wait(1000);

        cy.get('[data-cy=title-success]').should('be.visible');

        cy.get('button').get('[data-cy=button-home]').click();
        cy.url().should('eq', Cypress.config().baseUrl + '/publications');
    });

    it('should show error message on verification failure', () => {
        cy.intercept('POST', '**/users', { statusCode: 400, body: { message: 'Código inválido' } }).as('verifyRequest');

        cy.visit('/auth/verify?verification_code=ewN9fOewnoeM4e8mfpNqwo');

        cy.wait('@verifyRequest');

        cy.wait(1000);

        cy.get('[data-cy=title-error]').should('be.visible');
    });
});

describe('Change Password Request Tests', () => {
    beforeEach(() => {
        cy.visit('/auth/request-change-password');
    });

    it('should send change password request successfully', () => {
        cy.intercept('POST', '**/users', { statusCode: 200 }).as('changePasswordRequest');

        cy.get('[data-cy=email]').type('testuser@example.com');
        cy.get('[data-cy=submit]').click();

        cy.wait('@changePasswordRequest').its('response.statusCode').should('eq', 200);

        cy.get('[data-cy=title-success]').should('be.visible');
    });

    it('should handle user not found error', () => {
        cy.intercept('POST', '**/users', { statusCode: 404 }).as('changePasswordRequest');

        cy.get('[data-cy=email]').type('notfound@example.com');
        cy.get('[data-cy=submit]').click();

        cy.wait('@changePasswordRequest').should('have.property', 'response.statusCode', 404);

    });
});


describe('Change Password Tests', () => {
    beforeEach(() => {
        cy.visit('/auth/change-password?verification_code=123456');
    });

    it('should change password successfully', () => {
        cy.intercept('PATCH', '**/users/123456', { statusCode: 200 }).as('changePassword');

        cy.get('[data-cy=new-password]').type('hola').blur();
        cy.get('[data-cy=confirm-password]').type('hola').blur();
        cy.get('[data-cy=submit]').click({force:true});

        cy.wait('@changePassword');
    });

    it('should handle invalid token error', () => {
        cy.intercept('PATCH', '**/users/123456', { statusCode: 404 }).as('changePassword');

        cy.get('[data-cy=new-password]').type('NewPassword123!');
        cy.get('[data-cy=confirm-password]').type('NewPassword123!');
        cy.get('[data-cy=submit]').click({force:true});

        cy.wait('@changePassword');

    });
});
