--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.15
-- Dumped by pg_dump version 11.1 (Ubuntu 11.1-1.pgdg16.04+1)
SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: buginator_application; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_application (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(100) NOT NULL,
    company_id bigint NOT NULL
);


--
-- Name: buginator_application_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_application_id_seq OWNED BY public.buginator_application.id;


--
-- Name: buginator_company; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_company (
    id bigint NOT NULL,
    version bigint NOT NULL,
    address character varying(500),
    expiry_date timestamp without time zone,
    name character varying(100),
    token character varying(255) NOT NULL,
    unique_key character varying(255) NOT NULL,
    user_limit integer,
    payment_option_id bigint NOT NULL
);


--
-- Name: buginator_company_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_company_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_company_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_company_id_seq OWNED BY public.buginator_company.id;


--
-- Name: buginator_email_message; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_email_message (
    id bigint NOT NULL,
    version bigint NOT NULL,
    from_email character varying(255),
    pass character varying(255),
    smtp_port character varying(255),
    smtp_host character varying(255),
    ssl boolean NOT NULL,
    username character varying(255)
);


--
-- Name: buginator_email_message_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_email_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_email_message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_email_message_id_seq OWNED BY public.buginator_email_message.id;


--
-- Name: buginator_error; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_error (
    id bigint NOT NULL,
    version bigint NOT NULL,
    error_count integer NOT NULL,
    date_time timestamp without time zone NOT NULL,
    description character varying(1000),
    last_occurence timestamp without time zone NOT NULL,
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


--
-- Name: buginator_error_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_error_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_error_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_error_id_seq OWNED BY public.buginator_error.id;


--
-- Name: buginator_error_stack_trace; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_error_stack_trace (
    id bigint NOT NULL,
    version bigint NOT NULL,
    stack_trace character varying(255),
    stack_trace_order integer NOT NULL,
    error_id bigint NOT NULL
);


--
-- Name: buginator_error_stack_trace_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_error_stack_trace_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_error_stack_trace_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_error_stack_trace_id_seq OWNED BY public.buginator_error_stack_trace.id;


--
-- Name: buginator_notification; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_notification (
    id bigint NOT NULL,
    version bigint NOT NULL,
    seen boolean NOT NULL,
    error_id bigint NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: buginator_notification_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_notification_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_notification_id_seq OWNED BY public.buginator_notification.id;


--
-- Name: buginator_oauth2_client; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_oauth2_client (
    id bigint NOT NULL,
    version bigint NOT NULL,
    access_token_expiration integer,
    allowed_domains character varying(255),
    allowed_ips character varying(255),
    client_id character varying(255) NOT NULL,
    client_secret character varying(255) NOT NULL,
    type character varying(255)
);


--
-- Name: buginator_oauth2_client_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_oauth2_client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_oauth2_client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_oauth2_client_id_seq OWNED BY public.buginator_oauth2_client.id;


--
-- Name: buginator_password_reset; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_password_reset (
    id bigint NOT NULL,
    version bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    token character varying(255) NOT NULL,
    token_used boolean,
    user_id bigint NOT NULL
);


--
-- Name: buginator_password_reset_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_password_reset_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_password_reset_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_password_reset_id_seq OWNED BY public.buginator_password_reset.id;


--
-- Name: buginator_payment_option; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_payment_option (
    id bigint NOT NULL,
    version bigint NOT NULL,
    duration integer,
    max_users integer,
    name character varying(75),
    price double precision
);


--
-- Name: buginator_payment_option_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_payment_option_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_payment_option_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_payment_option_id_seq OWNED BY public.buginator_payment_option.id;


--
-- Name: buginator_permission; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_permission (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(50) NOT NULL
);


--
-- Name: buginator_permission_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_permission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_permission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_permission_id_seq OWNED BY public.buginator_permission.id;


--
-- Name: buginator_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(50) NOT NULL,
    company_id bigint
);


--
-- Name: buginator_role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_role_id_seq OWNED BY public.buginator_role.id;


--
-- Name: buginator_role_permission; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_role_permission (
    role_id bigint NOT NULL,
    permission_id bigint NOT NULL
);


--
-- Name: buginator_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_user (
    id bigint NOT NULL,
    version bigint NOT NULL,
    active boolean NOT NULL,
    email character varying(100) NOT NULL,
    name character varying(100) NOT NULL,
    pass character varying(100) NOT NULL,
    company_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- Name: buginator_user_agent_data; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_user_agent_data (
    id bigint NOT NULL,
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


--
-- Name: buginator_user_agent_data_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_user_agent_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_user_agent_data_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_user_agent_data_id_seq OWNED BY public.buginator_user_agent_data.id;


--
-- Name: buginator_user_application; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buginator_user_application (
    modify boolean,
    view boolean,
    application_id bigint NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: buginator_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.buginator_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buginator_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.buginator_user_id_seq OWNED BY public.buginator_user.id;


--
-- Name: buginator_application id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_application ALTER COLUMN id SET DEFAULT nextval('public.buginator_application_id_seq'::regclass);


--
-- Name: buginator_company id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_company ALTER COLUMN id SET DEFAULT nextval('public.buginator_company_id_seq'::regclass);


--
-- Name: buginator_email_message id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_email_message ALTER COLUMN id SET DEFAULT nextval('public.buginator_email_message_id_seq'::regclass);


--
-- Name: buginator_error id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_error ALTER COLUMN id SET DEFAULT nextval('public.buginator_error_id_seq'::regclass);


--
-- Name: buginator_error_stack_trace id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_error_stack_trace ALTER COLUMN id SET DEFAULT nextval('public.buginator_error_stack_trace_id_seq'::regclass);


--
-- Name: buginator_notification id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_notification ALTER COLUMN id SET DEFAULT nextval('public.buginator_notification_id_seq'::regclass);


--
-- Name: buginator_oauth2_client id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_oauth2_client ALTER COLUMN id SET DEFAULT nextval('public.buginator_oauth2_client_id_seq'::regclass);


--
-- Name: buginator_password_reset id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_password_reset ALTER COLUMN id SET DEFAULT nextval('public.buginator_password_reset_id_seq'::regclass);


--
-- Name: buginator_payment_option id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_payment_option ALTER COLUMN id SET DEFAULT nextval('public.buginator_payment_option_id_seq'::regclass);


--
-- Name: buginator_permission id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_permission ALTER COLUMN id SET DEFAULT nextval('public.buginator_permission_id_seq'::regclass);


--
-- Name: buginator_role id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_role ALTER COLUMN id SET DEFAULT nextval('public.buginator_role_id_seq'::regclass);


--
-- Name: buginator_user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user ALTER COLUMN id SET DEFAULT nextval('public.buginator_user_id_seq'::regclass);


--
-- Name: buginator_user_agent_data id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user_agent_data ALTER COLUMN id SET DEFAULT nextval('public.buginator_user_agent_data_id_seq'::regclass);


--
-- Name: buginator_application buginator_application_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_application
    ADD CONSTRAINT buginator_application_pkey PRIMARY KEY (id);


--
-- Name: buginator_company buginator_company_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_company
    ADD CONSTRAINT buginator_company_pkey PRIMARY KEY (id);


--
-- Name: buginator_email_message buginator_email_message_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_email_message
    ADD CONSTRAINT buginator_email_message_pkey PRIMARY KEY (id);


--
-- Name: buginator_error buginator_error_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_error
    ADD CONSTRAINT buginator_error_pkey PRIMARY KEY (id);


--
-- Name: buginator_error_stack_trace buginator_error_stack_trace_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_error_stack_trace
    ADD CONSTRAINT buginator_error_stack_trace_pkey PRIMARY KEY (id);


--
-- Name: buginator_notification buginator_notification_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_notification
    ADD CONSTRAINT buginator_notification_pkey PRIMARY KEY (id);


--
-- Name: buginator_oauth2_client buginator_oauth2_client_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_oauth2_client
    ADD CONSTRAINT buginator_oauth2_client_pkey PRIMARY KEY (id);


--
-- Name: buginator_password_reset buginator_password_reset_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_password_reset
    ADD CONSTRAINT buginator_password_reset_pkey PRIMARY KEY (id);


--
-- Name: buginator_payment_option buginator_payment_option_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_payment_option
    ADD CONSTRAINT buginator_payment_option_pkey PRIMARY KEY (id);


--
-- Name: buginator_permission buginator_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_permission
    ADD CONSTRAINT buginator_permission_pkey PRIMARY KEY (id);


--
-- Name: buginator_role_permission buginator_role_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_role_permission
    ADD CONSTRAINT buginator_role_permission_pkey PRIMARY KEY (role_id, permission_id);


--
-- Name: buginator_role buginator_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_role
    ADD CONSTRAINT buginator_role_pkey PRIMARY KEY (id);


--
-- Name: buginator_user_agent_data buginator_user_agent_data_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user_agent_data
    ADD CONSTRAINT buginator_user_agent_data_pkey PRIMARY KEY (id);


--
-- Name: buginator_user_application buginator_user_application_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user_application
    ADD CONSTRAINT buginator_user_application_pkey PRIMARY KEY (application_id, user_id);


--
-- Name: buginator_user buginator_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user
    ADD CONSTRAINT buginator_user_pkey PRIMARY KEY (id);


--
-- Name: buginator_role companyid_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_role
    ADD CONSTRAINT companyid_name UNIQUE (name, company_id);


--
-- Name: buginator_user uk_2xu66w5fjo2hnaomnevrohswq; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user
    ADD CONSTRAINT uk_2xu66w5fjo2hnaomnevrohswq UNIQUE (email);


--
-- Name: buginator_oauth2_client uk_35498igl4qq8wfacd6tb1q12e; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_oauth2_client
    ADD CONSTRAINT uk_35498igl4qq8wfacd6tb1q12e UNIQUE (client_id);


--
-- Name: buginator_password_reset uk_6lg5tgojbmlvs9hfjcvpiayia; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_password_reset
    ADD CONSTRAINT uk_6lg5tgojbmlvs9hfjcvpiayia UNIQUE (token);


--
-- Name: buginator_oauth2_client uk_6tustoyfal0unqtfgwibdwy73; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_oauth2_client
    ADD CONSTRAINT uk_6tustoyfal0unqtfgwibdwy73 UNIQUE (client_secret);


--
-- Name: buginator_company uk_8btdgoya2imqa1m4wp7o2d777; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_company
    ADD CONSTRAINT uk_8btdgoya2imqa1m4wp7o2d777 UNIQUE (name);


--
-- Name: buginator_company uk_abvmss85wpy79u10ouf4m6s6j; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_company
    ADD CONSTRAINT uk_abvmss85wpy79u10ouf4m6s6j UNIQUE (unique_key);


--
-- Name: buginator_payment_option uk_mk0a4xhamh2bhqiuf7dhfwcpb; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_payment_option
    ADD CONSTRAINT uk_mk0a4xhamh2bhqiuf7dhfwcpb UNIQUE (name);


--
-- Name: client_id_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX client_id_index ON public.buginator_oauth2_client USING btree (client_id);


--
-- Name: email_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX email_index ON public.buginator_user USING btree (email);


--
-- Name: error_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX error_index ON public.buginator_error_stack_trace USING btree (error_id);


--
-- Name: name_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX name_index ON public.buginator_company USING btree (name);


--
-- Name: token_key_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX token_key_index ON public.buginator_company USING btree (token, unique_key);


--
-- Name: user_error_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX user_error_index ON public.buginator_notification USING btree (user_id, error_id);


--
-- Name: user_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX user_index ON public.buginator_notification USING btree (user_id);


--
-- Name: buginator_error fk3uw2el9i6vr4500ng77jc9181; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_error
    ADD CONSTRAINT fk3uw2el9i6vr4500ng77jc9181 FOREIGN KEY (user_agent_data_id) REFERENCES public.buginator_user_agent_data(id);


--
-- Name: buginator_user fk7912uxfh9o3kn43fet4u3yd1h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user
    ADD CONSTRAINT fk7912uxfh9o3kn43fet4u3yd1h FOREIGN KEY (role_id) REFERENCES public.buginator_role(id);


--
-- Name: buginator_notification fk8kq9gtmcurhij7yc9g1tnk987; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_notification
    ADD CONSTRAINT fk8kq9gtmcurhij7yc9g1tnk987 FOREIGN KEY (user_id) REFERENCES public.buginator_user(id);


--
-- Name: buginator_company fk9wdq5x6v67srrr870lhn7hcuu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_company
    ADD CONSTRAINT fk9wdq5x6v67srrr870lhn7hcuu FOREIGN KEY (payment_option_id) REFERENCES public.buginator_payment_option(id);


--
-- Name: buginator_notification fkb4l0jnf7cwn52ijb6rrw7ur68; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_notification
    ADD CONSTRAINT fkb4l0jnf7cwn52ijb6rrw7ur68 FOREIGN KEY (error_id) REFERENCES public.buginator_error(id);


--
-- Name: buginator_role_permission fkbhpp0r1cc5nft3dgn2n351wna; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_role_permission
    ADD CONSTRAINT fkbhpp0r1cc5nft3dgn2n351wna FOREIGN KEY (permission_id) REFERENCES public.buginator_permission(id);


--
-- Name: buginator_role_permission fkc3dl7a5e1rpb8yvrb01knfcnj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_role_permission
    ADD CONSTRAINT fkc3dl7a5e1rpb8yvrb01knfcnj FOREIGN KEY (role_id) REFERENCES public.buginator_role(id);


--
-- Name: buginator_application fkfagsbokt64n9q3ej2n1u65utx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_application
    ADD CONSTRAINT fkfagsbokt64n9q3ej2n1u65utx FOREIGN KEY (company_id) REFERENCES public.buginator_company(id);


--
-- Name: buginator_role fkh4nbemagu446848iuh4l0gqv7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_role
    ADD CONSTRAINT fkh4nbemagu446848iuh4l0gqv7 FOREIGN KEY (company_id) REFERENCES public.buginator_company(id);


--
-- Name: buginator_password_reset fkj8ty3wj90ulpsoeo4y6tfngdo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_password_reset
    ADD CONSTRAINT fkj8ty3wj90ulpsoeo4y6tfngdo FOREIGN KEY (user_id) REFERENCES public.buginator_user(id);


--
-- Name: buginator_user fknn8xq0ac6qfhukk0yosj7q3di; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user
    ADD CONSTRAINT fknn8xq0ac6qfhukk0yosj7q3di FOREIGN KEY (company_id) REFERENCES public.buginator_company(id);


--
-- Name: buginator_user_application fkoi68r1wk1gsnncnqo2h0oq6w8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user_application
    ADD CONSTRAINT fkoi68r1wk1gsnncnqo2h0oq6w8 FOREIGN KEY (user_id) REFERENCES public.buginator_user(id);


--
-- Name: buginator_error fkriue0m0a47gs1vmndv7fcn63x; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_error
    ADD CONSTRAINT fkriue0m0a47gs1vmndv7fcn63x FOREIGN KEY (application_id) REFERENCES public.buginator_application(id);


--
-- Name: buginator_user_application fkt052nkwxlv8snpo6w74pywlh2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_user_application
    ADD CONSTRAINT fkt052nkwxlv8snpo6w74pywlh2 FOREIGN KEY (application_id) REFERENCES public.buginator_application(id);


--
-- Name: buginator_error_stack_trace fkt38yqwl3osx208y0dbwgga1jm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buginator_error_stack_trace
    ADD CONSTRAINT fkt38yqwl3osx208y0dbwgga1jm FOREIGN KEY (error_id) REFERENCES public.buginator_error(id);

--
-- PostgreSQL database dump complete
--

