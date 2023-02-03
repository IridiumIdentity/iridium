/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
-- 34d80292115d9a7ddc8cfddf7bafaf832b90baaa66d72a6fcdfd098d9254b38d
use identities;

SET @github_template_provider_id = UUID();
SET @github_instance_provider_id = UUID();

insert into external_identity_provider_templates (external_identity_provider_template_id, active, create_id, created, identity_request_base_url, profile_request_base_url,
                                                  client_id, client_secret, name, icon_path)
values (@github_template_provider_id, true, 'manual', NOW(), 'https://github.com/login/oauth/access_token',
        'https://api.github.com/user', '5e7a719eeb2c2324ef4d', '3091b48569366c15662b14cfc5a1a0e3d38dc69f', 'github', 'the path');


insert into application_types (application_type_id, active, create_id, created, name, description, requires_secret)
values  (UUID(), 1, 'manual', now(), 'Mobile or Native Application', 'Native or mobile apps', false);

SET @single_page_app_type_id = UUID();

SET @web_service_app_type_id = UUID();

insert into application_types (application_type_id, active, create_id, created, name, description, requires_secret)
values  (@single_page_app_type_id, 1, 'manual', now(), 'Single Page Application', 'Applications running in the browser.  Angular, React, Vue, etc.', false);

insert into application_types (application_type_id, active, create_id, created, name, description, requires_secret)
values  (@web_service_app_type_id, 1, 'manual', now(), 'Web Service Application', 'A regular web app running on a remote server. Spring, Ruby on Rails, etc.', true);


insert into application_types (application_type_id, active, create_id, created, name, description, requires_secret)
values  (UUID(), 1, 'manual', now(), 'Service Application', 'Applications not initiated by humans. Think bash scripts, cron jobs, IoT devices, and etc', true);

SET @iridium_tenant_id = UUID();

SET @iridium_login_descriptor_id = UUID();



insert into login_descriptors (login_descriptor_id,
                               active, create_id, created,
                               allow_github, allow_google, allow_apple, allow_microsoft,
                               icon_path, page_title, username_error_hint,
                               username_label, username_placeholder, username_type)
values (@iridium_login_descriptor_id,
        1, 'manual', now(),
        1, 0, 0, 0, '', 'Iridium', 'please enter a valid email',
        'Username', 'ex: you@somewhere.com', 'email');


insert into tenants (
    tenant_id, active, create_id, created,
    subdomain, login_descriptor_id, website_url)
values (
           @iridium_tenant_id, 1, 'manual', now(),
           'iridium', @iridium_login_descriptor_id, 'iridium.software'
       );

insert into external_identity_providers (external_identity_provider_id, external_identity_provider_template_id, active,
                                         create_id, created, access_token_request_base_url, profile_request_base_url,
                                         client_id, client_secret, redirect_uri, tenant_id, base_authorization_url,
                                         scope, icon_path, name)
values (@github_instance_provider_id, @github_template_provider_id, true, 'manual', NOW(),
        'https://github.com/login/oauth/access_token',
        'https://api.github.com/user', 'fcafdca0f26e2828da8f', '90ff70e93dc040cdaa240ac01e881c31c8a2bc61',
        'http://localhost:4300/callback', @iridium_tenant_id, 'https://github.com/login/oauth/authorize?', 'user:email',
        'thepath', 'Github');


insert into applications (
    application_id, active, create_id,
    created, client_id, home_page_url,
    name,
    redirect_uri, tenant_id, application_type_id)
values (
           uuid(), 1, 'manual', now(), 'xd4rtddkthdfh234r', 'http://localhost:4200',
           'iridium app', 'http://localhost:4300/callback', @iridium_tenant_id, @single_page_app_type_id
       );

insert into applications (
    application_id, active, create_id,
    created, client_id,
    name,
    redirect_uri, tenant_id, application_type_id)
values (
           uuid(), 1, 'manual', now(), 'tx4rtccpewsfh174r',
           'iridium app', 'http://localhost:4300/callback', @iridium_tenant_id, @web_service_app_type_id
       );
