INSERT INTO buginator_payment_option(id, duration, max_users, price, name, version) VALUES (1, 30, 5, 0.0, 'Trial', 1);

INSERT INTO buginator_role(id, name, version) VALUES (1, 'USER', 1);

INSERT INTO buginator_role(id, name, version) VALUES (2, 'MANAGER', 1);

INSERT INTO buginator_email_message(id, from_email, username, pass, smtp_host, smtp_port, ssl, version) VALUES (1, 'buginator.noreply@buginator.com', 'buginator.noreply@buginator.com', 'pass', 'mail.google.com', '547', false, 1);