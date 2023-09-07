export class ExchangeHeaderGeneratorService {

    generate() {
        return {
            headers: {
                'Accept': 'application/json'
            },
            method: 'POST'
        }
    }
}
