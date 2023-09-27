# Follow the previous steps to clone iridium code

### 1. You'll need the following to run iridium locally:
* [Node 18](https://nodejs.org/en)
* [Java 17](https://adoptium.net/temurin/releases/)
* [Docker](https://docs.docker.com/)

### 2. After you clone the iridum code into your local laptop, copy the application-h2.yml file into the irridium-core-server folder's resources folder

### 3. In your IntelliJ IDE, fill in the active profile to be h2

### 4. When you run the irridium-core-server, the h2 in memory db will be created.

### 5. Keep the irridium-core-server running,  use your browser navigate to http://localhost:8381/h2-ui

### 6. In the h2-ui login page:

#### JDBC URL:  jdbc:h2:mem:identities
#### User Name: iridium
#### Password: iridium

#### click the "Connect" button

### You will see the DB schema

## 7. If you want to input preset data into h2 DB for testing purpose,
### 1) Write insert statements in 02-insert-data-iridium.sql file.
### 2) Then, open 01-init-tables.yaml file,  uncomment all lines after line 1710 ( "- sqlFile" )