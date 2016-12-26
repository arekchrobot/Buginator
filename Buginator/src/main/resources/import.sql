INSERT INTO payment_option (id, version, duration, max_users, price, name) VALUES (1, 1, 30, 5, 0.00, 'Trial');

INSERT INTO buginator_permission (id, version, name) VALUES (1,1,'Company_manager');

INSERT INTO buginator_role (id, version, name) VALUES (1,1,'Company Manager');

INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,1);

INSERT INTO company(id, version, address, expiry_date, name, token, unique_key, user_limit,payment_option_id) VALUES
(1,1,'Test address', NOW() + INTERVAL '30 days','Test Company', 'Xqg5ueAkyuwLM6bVFR16vuThX83flR','lSFdQB4rQ79ydSt4C05qjFTmNHMnMi',5,1);

INSERT INTO buginator_user(id, version, active, email, name, pass, company_id, buginator_role_id) VALUES
(1,1,true,'asd@asd', 'User1', '$2a$11$ICWXs/nL4KcnsZ2org5KzOJEt8FpLP9ibJeC0mOeja8Lznv5YrFDi',1,1);

INSERT INTO application(id, version, name, company_id) VALUES (1,1, 'Test application', 1);

INSERT INTO user_application(modify, user_id, application_id) VALUES (true, 1, 1);

INSERT INTO buginator_error(id, version, error_count, date_time, description, last_occurence,sent_to_aggregators, severity, status,title, application_id) VALUES
(1,1,1,NOW()- INTERVAL '9 days','NullPointerException', NOW() - INTERVAL '9 days',false, 'ERROR', 'CREATED', 'Null has appeared', 1),
(2,1,1,NOW()- INTERVAL '8 days','NullPointerException', NOW() - INTERVAL '8 days',false, 'ERROR', 'CREATED', 'Null has appeared', 1),
(3,1,1,NOW()- INTERVAL '7 days','NullPointerException', NOW() - INTERVAL '7 days',false, 'ERROR', 'CREATED', 'Null has appeared', 1),
(4,1,1,NOW()- INTERVAL '7 days','NullPointerException', NOW() - INTERVAL '7 days',false, 'ERROR', 'CREATED', 'Null has appeared', 1),
(5,1,1,NOW()- INTERVAL '6 days','DataAccessException', NOW() - INTERVAL '6 days',false, 'ERROR', 'CREATED', 'Data Access Exception', 1),
(6,1,1,NOW()- INTERVAL '4 days','DataAccessException', NOW() - INTERVAL '4 days',false, 'ERROR', 'CREATED', 'Data Access Exception', 1),
(7,1,1,NOW()- INTERVAL '4 days','PermissionError', NOW() - INTERVAL '4 days',false, 'ERROR', 'CREATED', 'PermissionError', 1),
(8,1,1,NOW()- INTERVAL '4 days','Null', NOW() - INTERVAL '4 days',false, 'WARNING', 'CREATED', 'Null', 1),
(9,1,1,NOW()- INTERVAL '3 days','DatabaseError', NOW() - INTERVAL '3 days',false, 'CRITICAL', 'CREATED', 'Could not insert', 1),
(10,1,1,NOW()- INTERVAL '3 days','DatabaseError', NOW() - INTERVAL '3 days',false, 'CRITICAL', 'CREATED', 'Could not insert', 1),
(2,1,1,NOW(),'NullPointerException', NOW(),false, 'ERROR', 'CREATED', 'Null has appeared', 1);

