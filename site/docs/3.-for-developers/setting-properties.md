# Configuration
Iridium's core server needs a set of required properties to be set. These properties will need to be set at

* ```${ProjectRoot}/iridium-core-server/src/main/resources/application.properties```  

An example for local configuration is below. In future releases we will deprecate this method of setting properties. 

```properties
server.port=8381
logging.level.root=info
logging.level.software.iridium=debug
spring.jmx.enabled=false
logging.level.org.springframework.web=DEBUG
spring.mvc.log-request-details=true

spring.datasource.driver-class=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost/identities?useSSL=false
spring.datasource.username={DataSourceUsername}
spring.datasource.password={DataSourcePassword}
spring.datasource.tomcat.max-active=5
spring.datasource.test-while-idle=true
spring.datasource.validation-query=SELECT 1 FROM DUEL
spring.datasource.validation-interval=180000

spring.jpa.hibernate.ddl-auto=none

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
spring.jpa.properties.hibernate.globally_quoted_identifiers_skip_column_definitions = true
spring.jpa.properties.javax.persistence.schema-generation.create-source=metadata
spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=create.sql
spring.jpa.properties.javax.persistence.schema-generation.scripts.drop-target=create.sql
spring.jpa.properties.javax.persistence.schema-generation.database.action=drop-and-create
spring.jpa.properties.hibernate.hbm2ddl.delimiter=;

passwordRestToken.lifetimeHours=4
software.iridium.api.ttl.minutes=30
software.iridium.emailNotification.client.baseUrl=http://iridium.iridium.software:8381/
software.iridium.passwordReset.client.baseUrl=http://iridium.iridium.software:8381/
software.iridium.emailApi.baseUrl=http://localhost:8382/
software.iridium.identityApi.baseUrl=http://localhost:8381/

#disable default error page
server.error.whitelabel.enabled=false

github.accessTokenBaseUrl=https://github.com/login/oauth/access_token
```

Email specific settings are below.  Additionally, these can be added to the same `properties` file located at:
* `${ProjectRoot}/iridium-core-server/src/main/resources/application.properties`
```properties
spring.mail.host={YourSMTPHost}
spring.mail.username={YourSMTPUserName}
spring.mail.password={YourSMTPSecretKey}
spring.mail.properties.mail.transport.protocol=smtp
spring.mail.properties.mail.smtp.port=25
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.fromAddress=noreply@somwhere.com
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

