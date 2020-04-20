INSERT INTO buginator_company(unique_key, token, user_limit, expiry_date, name, address, payment_option_id, version) VALUES
    ('Q6JxGCjUM3fUbWX', 'KhpzXEcO3cmPCAr', 5, current_date + '1 month'::interval, 'Test Company', 'addres 123', 1, 1);

INSERT INTO buginator_user(name, email, active, company_id, pass, role_id, version) VALUES
    ('TestUser', 'test@gmail.com', true, (SELECT id FROM buginator_company WHERE name = 'Test Company'),
    '{def}$2a$10$ra/Scxal23zJrh.sh8nQP.LreuuTp0Ez8L9/aeQCA4AzRXct6zlea', (SELECT id FROM buginator_role WHERE name = 'MANAGER'), 1); --pass: 123

INSERT INTO buginator_application(name, version, company_id) VALUES
    ('Test Application', 1, 1);

INSERT INTO buginator_error(version, error_count, date_time, description, last_occurrence, severity, status, title, application_id) VALUES
    (1, 1, current_date - '1 month'::interval, 'NullPointerException', current_date - '10 days'::interval,
    'ERROR', 'CREATED', 'NullPointerException', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 3, current_date - '3 days'::interval, 'Payment was invalid in app', current_date - '3 days'::interval,
    'ERROR', 'ONGOING', 'Invalid payment', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 7, current_date - '10 days'::interval, 'Access denied', current_date - '10 days'::interval,
    'ERROR', 'CREATED', 'Access denied', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 1, current_date - '2 months'::interval, 'Fatal exception', current_date - '2 months'::interval,
    'ERROR', 'REOPENED', 'Fatal exception', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 1, current_date - '1 month'::interval, 'StackOverflowException', current_date - '1 month'::interval,
    'ERROR', 'CREATED', 'StackOverflowException', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 4, current_date - '1 month'::interval, 'StackOverflowException', current_date - '17 days'::interval,
    'ERROR', 'CREATED', 'StackOverflowException', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 1, current_date - '1 month'::interval, 'OutOfMemoryException', current_date - '6 days'::interval,
    'ERROR', 'CREATED', 'OutOfMemoryException', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 2, current_date - '1 month'::interval, 'Email was not sent', current_date - '2 days'::interval,
    'ERROR', 'REOPENED', 'EmailNotSentException', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 2, current_date - '1 month'::interval, 'No data', current_date - '1 month'::interval,
    'ERROR', 'ONGOING', 'DataNotFound', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 1, current_date - '1 month'::interval, 'Internal error', current_date - '10 days'::interval,
    'ERROR', 'ONGOING', 'Internal error', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 9, current_date - '1 month'::interval, 'Payment unsuccessful', current_date - '10 days'::interval,
    'ERROR', 'CREATED', 'Payment unsuccessful', (SELECT id FROM buginator_application WHERE name = 'Test Application')),
    (1, 1, current_date - '1 month'::interval, 'NullPointerException', current_date - '10 days'::interval,
    'ERROR', 'RESOLVED', 'NullPointerException', (SELECT id FROM buginator_application WHERE name = 'Test Application'));

INSERT INTO buginator_user_application(modify, view, application_id, user_id) VALUES
    (true, true, (SELECT id FROM buginator_application WHERE name = 'Test Application'), (SELECT id FROM buginator_user WHERE email = 'test@gmail.com'));