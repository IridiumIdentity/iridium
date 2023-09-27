pub mod url_generator_service {
    use dotenvy::dotenv;
    use std::env;

    pub fn get_iridium_auth_url(state: &str, code_challenge: &str) -> String {
        dotenv().ok();

        let base_url = env::var("RUST_IRIDIUM_BASE_URL").expect("IRIDIUM_BASE_URL must be set");
        let redirect_uri = env::var("RUST_PUBLIC_IRIDIUM_REDIRECT_URI")
            .expect("RUST_PUBLIC_IRIDIUM_REDIRECT_URI must be set");
        let client_id = env::var("RUST_PUBLIC_IRIDIUM_CLIENT_ID")
            .expect("RUST_PUBLIC_IRIDIUM_CLIENT_ID must be set");

        format!("{}login?response_type=code&state={}&redirect_uri={}&client_id={}&code_challenge_method=S256&code_challenge={}", base_url, state, redirect_uri, client_id, code_challenge)
    }
}
