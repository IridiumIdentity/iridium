export interface AuthenticationResponse {
    data: {
        userToken: string;
        userRefreshToken: string;
    }
}
