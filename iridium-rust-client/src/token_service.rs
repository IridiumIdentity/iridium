pub mod token_service {
    use reqwest::StatusCode;
    use serde::{Deserialize, Serialize};
    use std::env;

    #[derive(Debug, Deserialize, Serialize)]
    pub struct TokenResponse {
        access_token: String,
        token_type: String,
        expires_in: u64,
        refresh_token: Option<String>,
        scope: String,
    }

    pub async fn exchange_code_for_token(code: &str) -> Result<TokenResponse, reqwest::Error> {
        let client = reqwest::Client::new();
        let params = [
            (
                "client_id",
                env::var("CLIENT_ID").expect("CLIENT_ID must be set"),
            ),
            (
                "client_secret",
                env::var("CLIENT_SECRET").expect("CLIENT_SECRET must be set"),
            ),
            ("grant_type", "authorization_code".to_string()),
            ("code", code.to_string()),
            (
                "redirect_uri",
                env::var("RUST_PUBLIC_IRIDIUM_REDIRECT_URI")
                    .expect("RUST_PUBLIC_IRIDIUM_REDIRECT_URI must be set"),
            ),
        ];

        let response = client
            .post("https://auth-server.com/token")
            .form(&params)
            .send()
            .await?;

        if response.status() == StatusCode::OK {
            let token_response: TokenResponse = response.json().await?;
            Ok(token_response)
        } else {
            todo!("implements when status code is not OK");
        }
    }
}
