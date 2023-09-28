pub mod callback_service;
mod pkce_service;
mod state_generator_service;
mod token_service;
mod url_generator_service;

use rand::Rng;
use std::collections::HashMap;
use std::env;
use std::str::FromStr;
use reqwest::StatusCode;
use serde::{Deserialize, Serialize};
use tokio;
use warp::http::{HeaderMap, Uri};
use warp::{Error, Filter};
use warp::Reply;
use warp::reply::Response;


#[derive(Deserialize, Serialize, Debug)]
pub struct  User {
    pub code: String,
    pub message: String,
    pub data: Data,
}

#[derive(Deserialize, Serialize, Debug)]
pub struct Data {
    pub id: String,
    pub username: String,
    pub profile: Option<String>,
    pub userToken: Option<String>,
    pub roles: Vec<String>,
    pub tenantIds: Vec<String>,
}

impl warp::Reply for User {
    fn into_response(self) -> warp::reply::Response {
        let user = User {
            code: self.code,
            message: self.message,
            data: self.data,
        };
        let json = warp::reply::json(&user);
        json.into_response()
    }
}

impl warp::Reply for Data {
    fn into_response(self) -> warp::reply::Response {
        let data = Data {
            id: self.id,
            username: self.username,
            profile: self.profile,
            userToken: self.userToken,
            roles: self.roles,
            tenantIds: self.tenantIds,
        };
        let json = warp::reply::json(&data);
        json.into_response()
    }
}


pub fn authenticate_with_external_redirect(verifier: String) -> Option<Uri>   {
    let state = state_generator_service::state_generator::generate();
    let pkce_code = pkce_service::pkce_service::generate_code_challenge(&verifier);

    if let Ok(code_challenge) = pkce_code {
        let uri = Uri::from_str(
            &url_generator_service::url_generator_service::get_iridium_auth_url(
                    &state,
                    &code_challenge,
                ),
            )
            .unwrap();
        Some(uri)
    } else {
        eprintln!("Error generating code challenge");
        None
    }
}

pub async fn get_identity(token: &str) -> Result<User, warp::Rejection> {
    let client = reqwest::Client::new();
    let base_url = env::var("RUST_IRIDIUM_BASE_URL").expect("RUST_IRIDIUM_BASE_URL must be set");
    let identities_url = format!("{}identities", base_url);
    let mut headers = HeaderMap::new();
    let bearer = format!("Bearer {}", token);
    headers.insert(
        "Accept",
        "application/vnd.iridium.id.identity-response.1+json"
            .parse()
            .unwrap(),
    );

    headers.insert("Authorization", bearer.parse().unwrap());

   match client.get(&identities_url).headers(headers).send().await {
       Ok(response) if response.status() == StatusCode::OK => {
           let res = serde_json::from_str::<User>(&response.text().await.unwrap());
           println!("{:?}", res);
              Ok(res.unwrap())
       }
         Ok(response) => {
              eprintln!("Unexpected response status: {}", response.status());
              Err(warp::reject())
         }
            Err(e) => {
                eprintln!("Error making request: {}", e);
                Err(warp::reject())
            }
   }
}
pub fn generate_random_string() -> String {
    let random_string: String = rand::thread_rng()
        .sample_iter(&rand::distributions::Alphanumeric)
        .take(32)
        .map(char::from)
        .collect();
    random_string
}
pub fn add(left: usize, right: usize) -> usize {
    left + right
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        let result = add(2, 2);
        assert_eq!(result, 4);
    }
}
