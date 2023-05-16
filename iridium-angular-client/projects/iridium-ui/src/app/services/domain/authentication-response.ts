export interface AuthenticationResponse {
    data: { 
        userToken: string;
        userRefreshToken: string;
        passwordResetLink: string;
    }
}
