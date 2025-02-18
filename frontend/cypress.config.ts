import { defineConfig } from 'cypress'

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:8080',
    env: {
      apiUrl: 'http://localhost:8080/api'
    },
    supportFile: 'cypress/support/e2e.ts',
  },
})