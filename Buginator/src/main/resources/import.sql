INSERT INTO payment_option (duration, max_users, price, name) VALUES (30, 5, 0.00, 'Trial');

INSERT INTO buginator_permission (id, name) VALUES (1,'Company_manager');

INSERT INTO buginator_role (id, name) VALUES (1,'Company Manager');

INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,1);