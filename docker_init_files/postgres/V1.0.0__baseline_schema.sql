CREATE TABLE buginator_application (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    name character varying(100) NOT NULL,
    company_id bigint NOT NULL
);

CREATE TABLE buginator_company (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    address character varying(500),
    expiry_date timestamp without time zone,
    name character varying(100),
    token character varying(255) NOT NULL,
    unique_key character varying(255) NOT NULL,
    user_limit integer,
    payment_option_id bigint NOT NULL
);

CREATE TABLE buginator_email_message (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    from_email character varying(255),
    pass character varying(255),
    smtp_port character varying(255),
    smtp_host character varying(255),
    ssl boolean NOT NULL,
    username character varying(255)
);

CREATE TABLE buginator_error (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    error_count integer NOT NULL,
    date_time timestamp without time zone NOT NULL,
    description character varying(1000),
    last_occurrence timestamp without time zone NOT NULL,
    query_params character varying(750),
    request_headers character varying(800),
    request_method character varying(50),
    request_params character varying(1000),
    request_url character varying(200),
    severity character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    title character varying(200) NOT NULL,
    application_id bigint NOT NULL,
    user_agent_data_id bigint
);

CREATE TABLE buginator_error_stack_trace (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    stack_trace character varying(255),
    stack_trace_order integer NOT NULL,
    error_id bigint NOT NULL
);

CREATE TABLE buginator_notification (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    seen boolean NOT NULL,
    error_id bigint NOT NULL,
    user_id bigint NOT NULL
);

CREATE TABLE buginator_oauth2_client (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    access_token_expiration integer,
    allowed_domains character varying(255),
    allowed_ips character varying(255),
    client_id character varying(255) NOT NULL,
    client_secret character varying(255) NOT NULL,
    type character varying(255)
);

CREATE TABLE buginator_password_reset (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    token character varying(255) NOT NULL,
    token_used boolean,
    user_id bigint NOT NULL
);

CREATE TABLE buginator_payment_option (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    duration integer,
    max_users integer,
    name character varying(75),
    price double precision
);

CREATE TABLE buginator_permission (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    name character varying(50) NOT NULL
);

CREATE TABLE buginator_role (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    name character varying(50) NOT NULL,
    company_id bigint
);

CREATE TABLE buginator_role_permission (
    role_id bigint NOT NULL,
    permission_id bigint NOT NULL
);

CREATE TABLE buginator_user (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    active boolean NOT NULL,
    email character varying(100) NOT NULL,
    name character varying(100) NOT NULL,
    pass character varying(100) NOT NULL,
    company_id bigint NOT NULL,
    role_id bigint NOT NULL
);

CREATE TABLE buginator_user_agent_data (
    id bigserial PRIMARY KEY,
    version bigint NOT NULL,
    browser character varying(100),
    browser_family character varying(100),
    browser_full_version character varying(100),
    browser_vendor character varying(100),
    browser_version character varying(100),
    country character varying(100),
    device character varying(100),
    device_architecture character varying(100),
    device_type character varying(100),
    language character varying(100),
    manufacturer character varying(100),
    operating_system character varying(100),
    operating_system_description character varying(100),
    operating_system_vendor character varying(100),
    operating_system_version character varying(100),
    unknown_tokens character varying(100)
);

CREATE TABLE buginator_user_application (
    modify boolean,
    view boolean,
    application_id bigint NOT NULL,
    user_id bigint NOT NULL
);

ALTER TABLE buginator_role_permission
    ADD CONSTRAINT buginator_role_permission_pkey UNIQUE (role_id, permission_id);

ALTER TABLE buginator_user_application
    ADD CONSTRAINT buginator_user_application_pkey UNIQUE (application_id, user_id);

ALTER TABLE buginator_role
    ADD CONSTRAINT buginator_role_companyid_name_uniq UNIQUE (name, company_id);

ALTER TABLE buginator_user
    ADD CONSTRAINT buginator_user_email_uniq UNIQUE (email);

ALTER TABLE buginator_oauth2_client
    ADD CONSTRAINT buginator_oauth2_client_client_id_uniq UNIQUE (client_id);

ALTER TABLE buginator_password_reset
    ADD CONSTRAINT buginator_password_reset_token_uniq UNIQUE (token);

ALTER TABLE buginator_oauth2_client
    ADD CONSTRAINT buginator_oauth2_clien_client_secret_uniq UNIQUE (client_secret);

ALTER TABLE buginator_company
    ADD CONSTRAINT buginator_company_name_uniq UNIQUE (name);

ALTER TABLE buginator_company
    ADD CONSTRAINT buginator_company_unique_key_uniq UNIQUE (unique_key);

ALTER TABLE buginator_payment_option
    ADD CONSTRAINT buginator_payment_option_name_uniq UNIQUE (name);

CREATE INDEX client_id_index ON buginator_oauth2_client (client_id);

CREATE INDEX email_index ON buginator_user (email);

CREATE INDEX error_index ON buginator_error_stack_trace (error_id);

CREATE INDEX name_index ON buginator_company (name);

CREATE INDEX token_key_index ON buginator_company (token, unique_key);

CREATE INDEX user_error_index ON buginator_notification (user_id, error_id);

CREATE INDEX user_index ON buginator_notification (user_id);

ALTER TABLE buginator_error
    ADD CONSTRAINT buginator_error_user_agent_data_id_fk FOREIGN KEY (user_agent_data_id) REFERENCES buginator_user_agent_data(id);

ALTER TABLE buginator_user
    ADD CONSTRAINT buginator_user_role_id_fk FOREIGN KEY (role_id) REFERENCES buginator_role(id);

ALTER TABLE buginator_notification
    ADD CONSTRAINT buginator_notification_user_id_fk FOREIGN KEY (user_id) REFERENCES buginator_user(id);

ALTER TABLE buginator_company
    ADD CONSTRAINT buginator_company_payment_option_id_fk FOREIGN KEY (payment_option_id) REFERENCES buginator_payment_option(id);

ALTER TABLE buginator_notification
    ADD CONSTRAINT buginator_notification_error_id_fk FOREIGN KEY (error_id) REFERENCES buginator_error(id);

ALTER TABLE buginator_role_permission
    ADD CONSTRAINT buginator_role_permission_permission_id_fk FOREIGN KEY (permission_id) REFERENCES buginator_permission(id);

ALTER TABLE buginator_role_permission
    ADD CONSTRAINT buginator_role_permission_role_id_fk FOREIGN KEY (role_id) REFERENCES buginator_role(id);

ALTER TABLE buginator_application
    ADD CONSTRAINT buginator_application_company_id_fk FOREIGN KEY (company_id) REFERENCES buginator_company(id);

ALTER TABLE buginator_role
    ADD CONSTRAINT buginator_role_company_id_fk FOREIGN KEY (company_id) REFERENCES buginator_company(id);

ALTER TABLE buginator_password_reset
    ADD CONSTRAINT buginator_password_reset_user_id_fk FOREIGN KEY (user_id) REFERENCES buginator_user(id);

ALTER TABLE buginator_user
    ADD CONSTRAINT buginator_user_company_id_fk FOREIGN KEY (company_id) REFERENCES buginator_company(id);

ALTER TABLE buginator_user_application
    ADD CONSTRAINT buginator_user_application_user_id_fk FOREIGN KEY (user_id) REFERENCES buginator_user(id);

ALTER TABLE buginator_error
    ADD CONSTRAINT buginator_error_application_id_fk FOREIGN KEY (application_id) REFERENCES buginator_application(id);

ALTER TABLE buginator_user_application
    ADD CONSTRAINT buginator_user_application_application_id_fk FOREIGN KEY (application_id) REFERENCES buginator_application(id);

ALTER TABLE buginator_error_stack_trace
    ADD CONSTRAINT buginator_error_stack_trace_error_id_fk FOREIGN KEY (error_id) REFERENCES buginator_error(id);