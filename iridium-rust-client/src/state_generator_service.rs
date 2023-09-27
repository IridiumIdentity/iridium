pub mod state_generator {
    use uuid::Uuid;

    pub fn generate() -> String {
        let random_string = Uuid::new_v4();
        hex::encode(random_string.as_bytes())
    }
}
