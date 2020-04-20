INSERT INTO buginator_payment_option(duration, max_users, price, name, version) VALUES
    (30, 5, 0.0, 'Trial', 1);

INSERT INTO buginator_permission(name, version) VALUES
    ('access', 1),
    ('create_application', 1),
    ('read_application', 1),
    ('app_show_notification',1),
    ('app_manage_users',1);

INSERT INTO buginator_role(name, version) VALUES
    ('USER', 1),
    ('MANAGER', 1);

INSERT INTO buginator_role_permission(role_id, permission_id) VALUES
    ((SELECT id FROM buginator_role WHERE name = 'MANAGER'), (SELECT id FROM buginator_permission WHERE name = 'access')),
    ((SELECT id FROM buginator_role WHERE name = 'MANAGER'), (SELECT id FROM buginator_permission WHERE name = 'create_application')),
    ((SELECT id FROM buginator_role WHERE name = 'MANAGER'), (SELECT id FROM buginator_permission WHERE name = 'read_application')),
    ((SELECT id FROM buginator_role WHERE name = 'MANAGER'), (SELECT id FROM buginator_permission WHERE name = 'app_show_notification')),
    ((SELECT id FROM buginator_role WHERE name = 'MANAGER'), (SELECT id FROM buginator_permission WHERE name = 'app_manage_users')),
    ((SELECT id FROM buginator_role WHERE name = 'USER'), (SELECT id FROM buginator_permission WHERE name = 'access')),
    ((SELECT id FROM buginator_role WHERE name = 'USER'), (SELECT id FROM buginator_permission WHERE name = 'read_application'));

INSERT INTO buginator_oauth2_client(client_id, client_secret, access_token_expiration, type, allowed_domains, allowed_ips, version) VALUES
    ('buginatorWebApp', '{def}$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC', 3600, 'WEB', 'localhost:8080,127.0.0.1:8080,localhost:4200,127.0.0.1:4200', null, 1); -- secret: secret