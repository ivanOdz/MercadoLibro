describe('Authentication Tests', () => {
    beforeEach(() => {
        cy.visit('/auth/login');
    });

    it('should log in successfully with valid credentials', () => {
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

        cy.get('[data-cy=username]').type('testuser');
        cy.get('[data-cy=password]').type('password123');
        cy.get('[data-cy=submit]').click();

        cy.wait('@loginRequest');

        cy.url().should('not.include', '/auth');

        cy.window().then((win) => {
            expect(win.sessionStorage.getItem('accessToken')).to.eq('fake_access_token');
            expect(win.sessionStorage.getItem('refreshToken')).to.eq('fake_refresh_token');
        });
    });


    // FIXME: Manejo de errores
    it('should show an error on invalid login', () => {
        cy.intercept('HEAD', '**/book_models', (req) => {
            req.reply({
                statusCode: 401,
            });
        }).as('loginRequest');

        cy.get('[data-cy=username]').type('wronguser');
        cy.get('[data-cy=password]').type('wrongpass');
        cy.get('[data-cy=submit]').click();

        cy.wait('@loginRequest');

        cy.get('[data-cy="username"]')
            .then(() => {
                cy.get('.error-message').should('be.visible')
            });

        cy.get('[data-cy="password"]')
            .then(() => {
                cy.get('.error-message')
                    .should('be.visible')
            });
    });

    it('should remember login credentials when Remember Me is checked', () => {
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

        cy.get('[data-cy=username]').type('testuser')
        cy.get('[data-cy=password]').type('password123');
        cy.get('[data-cy=remember-me]').click();
        cy.get('[data-cy=submit]').click();

        cy.window().its('localStorage').should('have.property', 'accessToken');
    });

    it('should log out correctly', () => {
        cy.login('testuser', 'password123');
        cy.visit('/publications');

        cy.get('#profile-button').click()

        cy.get('#logout-item').click();

        cy.window().its('sessionStorage').should('not.have.property', 'accessToken');
        cy.window().its('localStorage').should('not.have.property', 'accessToken');
        cy.url().should('include', '/publications');
    });
});

