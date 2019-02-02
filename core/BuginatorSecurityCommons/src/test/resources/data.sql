INSERT INTO buginator_payment_option(id, duration, max_users, price, name, version) VALUES (1, 30, 5, 0.0, 'Trial', 1);

INSERT INTO buginator_role(id, name, version) VALUES (2, 'MANAGER', 1);

INSERT INTO buginator_company(id, unique_key, token, user_limit, expiry_date, name, address, payment_option_id, version) VALUES (1, 'Q6JxGCjUM3fUbWX', 'KhpzXEcO3cmPCAr', 5, current_date + 20, 'Test Company', 'addres 123', 1, 1);