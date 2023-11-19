![iridium system overview](../images/iridium-overview.png "iridium system overview")
# Iridium Database

The Iridium database stores the data and metadata for an Iridium instance. Each Iridium instance uses its own database, which is hosted in a separate container to the primary application. The database can be treated as a simple repository rather than an independent data management system.

In this document, the physical structure of the database will be laid out for both cursory reference and as a rough guide for developers aiming to reconfigure the system.

## Abstract Overview
![This is the Primary Abstract Entity Relationship Diagram. There are six entities, defined below. The relationships are as follows: Managing Identity has a many-to-many relationship with Tenant. Customer Identity has a many relationship to one Tenant. Application has a many relationship to one tenant, and many-to-many with Customer Identities. External Identity Providers and Application Type both have a many relationship to one Application.](../images/iridiumdb-overview.png)

<sup>Figure 1: Abstract iridium database breakdown.</sup> 

Iridium uses a fairly linear data flow between its entities to achieve tasks. A third party `external identity provider` such as google or github is linked to an `application` secured with Iridium. Within a `tenant` container, `identities` of users are verified using Iridium. Within each `tenant` are `customer identities` (end-users) and `managing identities` (tenant-level system administrators).

* **Managing Identity:** System administrators for a tenant. There can be multiple managing identities per tenant.
* **Customer Identity:** Standard user of Iridium. At the level of tenant, each user is limited to one identity. However, users can make an identity for an unlimited number of tenants.
* **Tenant:** A tenant is a container for clients, representing an environment or organization. A single company using Iridium can register multiple tenants.
* **Application:** Applications are the specific apps secured with Iridium. They are bound to a tenant.
* **Application Type:** The types of applications securable by Iridium. Currently, this range is limited to a single page application such as Angular.
* **External Identity Provider:** Third party identity providers using Iridium to authenticate user credentials and gate access for users. Other Iridium instances can be external identity providers.

## System Breakdown
 The default Iridium database does contain constrained relationships, but is primarily controlled through the java application. 

 Developers who prefer using a relational database management system such as MariaDB to control data flow can still configure Iridium to their needs. Iridium is made to be manipulated and retooled to meet different developer's needs and standards.

*A Note on Reading the Diagrams*

Some entities reference a key attribute from another entity without using it as an identifying key. Such attributes will be marked "ref." Real database entities directly correlating to roles in the abstract data flow will be `highlighted`. 

---
### Resource Access Entities
![This is a Resource Access Entity Relationship Diagram. Three entities are present, defined in a written list below. Relationships depicted are as follows: access_tokens have a many, optional relationship to refresh_tokens.](../images/iridiumdb-resource-access-entities.png)

<sup>figure 2: Resource Access Entity Relationship Diagram</sup>

Data Iridium uses to allow access to protected resources.

* **[refresh_tokens]:** Allows access tokens to be used again.
* **[access_tokens]:** Holds the hashes that allow access to protected resources.
* **[authorization_codes]:** Codes exchanged between client and Iridium, used by the client to request access and refresh tokens.

---
### Tenants
![This is a Tenant Entity Relationship Diagram. Two entities are present, defined in a written list below. Relationships depicted are as follows: login_descriptors has a many, optional relationship to refresh_tokens.](../images/iridiumdb-tenants.png)

<sup>figure 3: Tenant Entity Relationship Diagram</sup>

A tenant is a container for clients, representing an environment or organization. A single company using Iridium can register multiple tenants.

* **[login_descriptors]:** Describes what the login page holds for each tenant.
* **`[tenants]:`** Stores defining data for a tenant container. Tenant names are used in the tenant's login URL, so tenant names in the database must be URL safe.
---
### External Identity Providers
![This is an External Identity Provider Entity Relationship Diagram. Four entities are present, three of which are defined in the list below, one of which is defined in the tenants section. The relationships are as follows: external_identity_provider_parameter_templates has a many relationship with external_identity_provider_templates. external_identity_providers has a many relationship with external_identity_provider_templates. external_identity_providers also has a many relationship with tenants.](../images/iridiumdb-external-identity-providers.png)

<sup>figure 4: External Identity Provider Entity Relationship Diagram</sup>

Entities holding configuration for external identity providers (google, github, facebook, etc) authenticating using Iridium. Other Iridium instances are registerable as external identity providers.

* **[external_identity_provider_templates]:** Templates for potential external identity providers.
* **[external_identity_provider_parameter_templates]:** Prebuilt parameter configurations for adding to an external identity provider.
* **`[external_identity_providers]:`** Holds the seed data for an OpenID provider to authenticate with Iridium.
---
### External Identity Provider Workspace
![This is an External Identity Provider Workspace Entity Relationship Diagram. There are five entities, four of which are defined below, and one of which was previously defined in the external identity providers section. The relationships are as follows: external_identity_providers defines access_token_parameters and authorization_parameters, one provider to many definitions. external_identity_provider_parameters has a many relationship with external_identity_providers. in_progress_external_identity_provider_authorizations has a many, optional relationship with external_identity_providers.](../images/iridiumdb-exidpr-workspace.png)

<sup>figure 5:External Identity Provider Workspace Entity Relationship Diagram</sup>

Entities containing additional configuration and dynamic functions for an external identity provider.

* **[access_token_parameters]:** Configuration for access tokens specific to an external identity provider.
* **[authorization_parameters]:** Configuration for authorization codes, specific to an external identity provider.
* **[external_identity_provider_parameters]:** Additional configuration for a specific external identity provider.
* **[in_progress_external_identity_provider_authorizations]:** Container for the dynamic function of authorizing with an external identity provider.
---
### Identities
![This is an Identities Entity Relationship Diagram. Five entities are defined below, one is previously defined in tenants, and another is previously defined in external identity providers. The relationships are as follows: tenants_identities_xref has a many relationship with single instances of tenants and identities. Identities has a many relationship with single instances of external_identity_providers and profiles.](../images/iridiumdb-identities.png)

<sup>figure 6:Identities Entity Relationship Diagram</sup>

Client authenticating with an external identity provider via Iridium. A single client may have multiple identities for different external identity providers.

* **[profiles]:** Holds basic client registration data.
* **`[identities]:`** Clients registered with a specific external identity provider, contains both managing and customer identities.
* **[tenants_identities_xref]:** tenants/identities cross-reference.
---
### Identity Workspace
![This is an Identities Workspace Entity Relationship Diagram. Eight entities are in the diagram, seven of which are defined below, and one of which was previously defined in identities. The relationships are as follows: identities has a single-to-many relationship with identity_email_addresses, authentications, and identity_property. Email_verification_tokens has a many, optional relationship with single identity_email_addresses. Many roles_identities_xref are defined by single identities and roles.](../images/iridiumdb-id-workspace.png)

<sup>figure 7:Identities Workspace Entity Relationship Diagram</sup>

Additional configuration entities and containers for dynamic functions related to identities.

* **[identity_create_session_details]:** Metadata storage for a single instance of registering an identity.
* **[authentications]:** active authorizations/codes/etc for user
* [**identity_property]:** additional configuration for a specific user.
* **[identity_email_addresses]:** emails associated with a user.
* **[email_verification_tokens]:** verification for registered user emails.
* **`[roles]:`** User roles, contains delineations between managing and customer identities.
* **[roles_identities_xref]:** roles/identities cross-reference.
---
### Applications
![This is an Applications Entity Relationship Diagram. There are six entities, five of which are defined below, and one previously defined in the identities section. The relationships are as follows: client_secrets, scopes, and application_types all have a many, optional relationship to single applications. identities_applications and scopes both have a many, optional relationship to single identities.](../images/iridiumdb-applications.png)

<sup>figure 8:Applications Entity Relationship Diagram </sup>

Applications are the specific apps secured with Iridium. They are bound to a tenant.

* **[application_types]:** All types of applications securable via Iridium
* **`[applications]:`** Applications secured with Iridium. Applications are specific to a tenant.
* **[client_secrets]:** client secret codes associated with an application
* **[scopes]:** access parameters for an identity particular to an application
* **[identities_applications]:** identities/applications cross-reference.
