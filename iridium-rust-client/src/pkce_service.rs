pub mod pkce_service {
    use digest::Digest;
    use sha2::Sha256;

    pub fn generate_code_challenge(verifier: &str) -> Result<String, Box<dyn std::error::Error>> {
        generate_base64_encoded_challenge(verifier)
    }

    fn generate_base64_encoded_challenge(
        verifier: &str,
    ) -> Result<String, Box<dyn std::error::Error>> {
        let signature = generate_sha256_signature(verifier)?;
        Ok(base64::encode_config(signature, base64::URL_SAFE))
    }

    fn generate_sha256_signature(input: &str) -> Result<Vec<u8>, Box<dyn std::error::Error>> {
        let mut hasher = Sha256::new();
        hasher.update(input);
        Ok(hasher.finalize().to_vec())
    }
}
