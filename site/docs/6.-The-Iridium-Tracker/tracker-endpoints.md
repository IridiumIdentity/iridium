
## Get new users, grouped by provider over a specified range.

* Method: `GET`
* Endpoint: `${serverRoot}/tenants/${tenant-id}/account-types`

### Query Params
| Query Parameter | Type | Description                                                                                                         |
  |-----------------|------|---------------------------------------------------------------------------------------------------------------------|
| from            | Date | **Required** This is expected to be in the format of `"yyyy-MM-dd"`.  It must be within 7 days of the current date. |


### Path Variables
| Path Variable | Type   | Description                                                     |
  |------|--------|-----------------------------------------------------------------|
| tenant-id     | string | **Required** The tenant identifier. This should be a valid uuid |


### Headers
| Header        | Value                     | Description                         |
|---------------|---------------------------|-------------------------------------|
| Authorization | Bearer ${YourBearerToken} | **Required** The users bearer token |



### Response:
* If the request is successful the response will look similar to the following:
```json
HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: no-store

[
  {
    "name":"GitHub",
    "value":56
  },
  {
    "name":"Google",
    "value":34
  }
]
```

### An example of how to invoke is below:
```shell
$ curl --location 'localhost:8382/tenants/${SomeTenantId}/account-types?from=2023%2F09%2F28' \
--header 'Authorization: Bearer ${YourBearerToken}' \
--header 'Accept: application/json'
```
