
export const environment = {
  production: false,
  iridium: {
    domain: 'http://localhost:8381/',
    redirectUri: 'http://localhost:4200/callback',
    successfulAuthDestination: '/dashboard',
    clientId: 'YOUR_CLIENT_ID',
    errorPath: '/error'
  }
};

