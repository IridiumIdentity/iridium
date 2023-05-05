
export const environment = {
  production: false,
  authenticationApiBaseUrl: 'http://localhost:8381/',
  githubAuthBaseUrl: 'https://github.com/login/oauth/authorize?',
  githubClientId: '5e7a719eeb2c2324ef4d',
  githubRedirectUrl: 'http://localhost:4201/login/callback',
  githubScope: 'user:email',
  iridium: {
    domain: 'http://iridium.iridium.software:8381/',
    clientId: 'xd4rtddkthdfh234r',
    apiUri: 'http://localhost:3001',
    appUri: 'http://localhost:4300',
    redirectUri: 'http://localhost:4300/callback',
    errorPath: '/error'
  }
};

