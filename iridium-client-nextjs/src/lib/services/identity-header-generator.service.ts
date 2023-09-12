export class IdentityHeaderGeneratorService {
    public generate(token: string) {
        return {
            headers: {
                'Accept': 'application/vnd.iridium.id.identity-response.1+json', 'Authorization': 'Bearer ' + token,
            }, method: 'GET'
        };
    }
}
