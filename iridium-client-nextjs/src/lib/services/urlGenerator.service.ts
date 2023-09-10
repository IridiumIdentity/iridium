export class UrlGeneratorService {
    retrieveIridiumAuthUrl(state: string, codeChallenge: string): string {
        const baseUrl = process.env.NEXT_PUBLIC_IRIDIUM_DOMAIN
        const redirectUri = process.env.NEXT_PUBLIC_IRIDIUM_REDIRECT_URI
        const clientId = process.env.NEXT_PUBLIC_IRIDIUM_CLIENT_ID

        return `${baseUrl}login?response_type=code&state=${state}&redirect_uri=${redirectUri}&client_id=${clientId}&code_challenge_method=S256&code_challenge=${codeChallenge}`;
    }
}
