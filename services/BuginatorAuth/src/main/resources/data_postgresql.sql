INSERT INTO buginator_payment_option(id, duration, max_users, price, name, version) VALUES (1, 30, 5, 0.0, 'Trial', 1);

INSERT INTO buginator_company(id, unique_key, token, user_limit, expiry_date, name, address, payment_option_id, version) VALUES (1, 'Q6JxGCjUM3fUbWX', 'KhpzXEcO3cmPCAr', 5, current_date + '1 month'::interval, 'Test Company', 'addres 123', 1, 1);

INSERT INTO buginator_permission(id, name, version) VALUES (1, 'ACCESS', 1);

INSERT INTO buginator_role(id, name, version) VALUES (1, 'USER', 1);

INSERT INTO buginator_role(id, name, version) VALUES (2, 'MANAGER', 1);

INSERT INTO buginator_role_permission(role_id, permission_id) VALUES (1, 1);

INSERT INTO buginator_user(id, name, email, active, company_id, pass, role_id, version) VALUES (1, 'TestUser', 'test@gmail.com', true, 1, '{def}$2a$10$ra/Scxal23zJrh.sh8nQP.LreuuTp0Ez8L9/aeQCA4AzRXct6zlea', 1, 1); --pass: 123

INSERT INTO buginator_oauth2_client(id, client_id, client_secret, access_token_expiration, type, allowed_domains, allowed_ips, version) VALUES (1, 'buginatorWebApp', '{def}$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC', 3600, 'WEB', 'localhost:8080,127.0.0.1:8080,localhost:4200,127.0.0.1:4200', null, 1); -- secret: secret