INSERT INTO payment_option (version, duration, max_users, price, name) VALUES (1, 30, 5, 0.00, 'Trial');

INSERT INTO buginator_permission (version, name) VALUES (1,'read_application');
INSERT INTO buginator_permission (version, name) VALUES (1,'create_application');
INSERT INTO buginator_permission (version, name) VALUES (1,'app_manage_users');
INSERT INTO buginator_permission (version, name) VALUES (1,'app_show_notification');
INSERT INTO buginator_permission (version, name) VALUES (1,'app_modify_notification');
INSERT INTO buginator_permission (version, name) VALUES (1,'manage_role');
INSERT INTO buginator_permission (version, name) VALUES (1,'manage_user');
INSERT INTO buginator_permission (version, name) VALUES (1,'app_show_notification_log');

INSERT INTO buginator_role (version, name) VALUES (1,'Company Manager');

INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,1);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,2);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,3);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,4);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,5);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,6);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,7);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (1,8);

INSERT INTO company(version, address, expiry_date, name, token, unique_key, user_limit,payment_option_id) VALUES
  (1,'Test address', NOW() + INTERVAL '30 days','Test Company', 'Xqg5ueAkyuwLM6bVFR16vuThX83flR','lSFdQB4rQ79ydSt4C05qjFTmNHMnMi',5,1),
  (1,'Test address', NOW() + INTERVAL '30 days','Test Company 2', 'Xqg5ueAkyuoLM6bVFR16vuThX83flR','lSFdQB4lQ79ydSt4C05qjFTmNHMnMi',5,1);

INSERT INTO buginator_role (version, name, company_id) VALUES (1, 'App 1 Developer', 1);
INSERT INTO buginator_role (version, name, company_id) VALUES (1, 'App 3 Developer', 2);

INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (2,2);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (2,3);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (2,7);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (3,2);
INSERT INTO buginator_role_permission (role_id, permission_id) VALUES (3,3);

INSERT INTO buginator_user(version, active, email, name, pass, company_id, buginator_role_id) VALUES
(1,true,'asd@asd', 'User1', '$2a$11$ICWXs/nL4KcnsZ2org5KzOJEt8FpLP9ibJeC0mOeja8Lznv5YrFDi',1,1),
(1,true,'asd2@asd', 'User2', '$2a$11$ICWXs/nL4KcnsZ2org5KzOJEt8FpLP9ibJeC0mOeja8Lznv5YrFDi',1,2),
(1,false,'asd3@asd', 'User3', '$2a$11$ICWXs/nL4KcnsZ2org5KzOJEt8FpLP9ibJeC0mOeja8Lznv5YrFDi',1,1),
(1,true,'asd4@asd', 'User4', '$2a$11$ICWXs/nL4KcnsZ2org5KzOJEt8FpLP9ibJeC0mOeja8Lznv5YrFDi',2,1);


INSERT INTO application(version, name, company_id) VALUES
(1, 'Test application', 1),
(1, 'Test application 2', 1),
(1, 'Test application 3', 2);

INSERT INTO user_application(modify, user_id, application_id) VALUES
(true, 1, 1),
(false, 1, 2);

INSERT INTO user_agent_data(version, browser, browser_family, browser_full_version, browser_vendor,
                            browser_version, device, device_architecture, device_type, manufacturer, operating_system, operating_system_description,
                            operating_system_vendor, operating_system_version, country, language) VALUES
(1, 'Firefox', 'Firefox', '40.1','Mozilla','40.1','PC','64bits','COMPUTER','(Windows)','Windows NT','Windows','Microsoft','7', 'Poland', 'PL');

INSERT INTO buginator_error(version, error_count, date_time, description, last_occurence, severity, status,title, application_id, user_agent_data_id, request_url, query_params, request_method, request_params, request_headers) VALUES
  (1,6,NOW()- INTERVAL '9 days','NullPointerException', NOW() - INTERVAL '9 days', 'ERROR', 'CREATED', 'Null has appeared', 1, 1, 'http://google.com/firstUrlEver', 'a=b&x=cc&acb=dcf','GET',NULL,'host=api.mobl-apps.com\nx-real-ip=193.105.74.47\nx-forwarded-for=193.105.74.47\naccept-encoding=gzip,deflate'),
  (1,1,NOW()- INTERVAL '8 days','NullPointerException', NOW() - INTERVAL '8 days', 'ERROR', 'RESOLVED', 'Null has appeared', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '7 days','NullPointerException', NOW() - INTERVAL '7 days', 'ERROR', 'ONGOING', 'Null has appeared', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '7 days','NullPointerException', NOW() - INTERVAL '7 days', 'ERROR', 'REOPENED', 'Null has appeared', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '6 days','DataAccessException', NOW() - INTERVAL '6 days', 'ERROR', 'CREATED', 'Data Access Exception', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '4 days','DataAccessException', NOW() - INTERVAL '4 days', 'ERROR', 'CREATED', 'Data Access Exception', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '4 days','PermissionError', NOW() - INTERVAL '4 days', 'ERROR', 'CREATED', 'PermissionError', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '4 days','Null', NOW() - INTERVAL '4 days', 'WARNING', 'CREATED', 'Null', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '3 days','DatabaseError', NOW() - INTERVAL '3 days', 'CRITICAL', 'CREATED', 'Could not insert', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '3 days','DatabaseError', NOW() - INTERVAL '3 days', 'CRITICAL', 'CREATED', 'Could not insert', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW(),'NullPointerException', NOW(), 'ERROR', 'CREATED', 'Null has appeared', 1, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '14 days','NullPointerException', NOW() - INTERVAL '14 days', 'ERROR', 'CREATED', 'Null has appeared', 2, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '11 days','NullPointerException', NOW() - INTERVAL '11 days', 'ERROR', 'CREATED', 'Null has appeared', 2, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '4 days','NullPointerException', NOW() - INTERVAL '4 days', 'ERROR', 'CREATED', 'Null has appeared', 2, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '3 days','NullPointerException', NOW() - INTERVAL '3 days', 'ERROR', 'CREATED', 'Null has appeared', 2, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW()- INTERVAL '3 days','DataAccessException', NOW() - INTERVAL '3 days', 'ERROR', 'CREATED', 'Data Access Exception', 2, NULL, NULL, NULL, NULL, NULL, NULL),
  (1,1,NOW(),'DataAccessException', NOW(), 'ERROR', 'CREATED', 'Data Access Exception', 3, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO buginator_error_stack_trace(version, stack_trace, stack_trace_order, buginator_error_id) VALUES
(1, 'Exception in thread "main" java.lang.NullPointerException', 1, 1),
(1, 'at com.ffm.files.Main.main(Main.java:52)', 3, 1),
(1, 'at com.ffm.files.Main$Test.test(Main.java:60)', 2, 1),
(1, 'Caused by: java.lang.StackOverflowError', 4, 1),
(1, 'at java.lang.ClassLoader.defineClass1(Native Method)', 5, 1),
(1, 'at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:691)', 6, 1);

INSERT INTO buginator_notification(version, seen, buginator_error_id, buginator_user_id) VALUES
(1, false, 11, 1),
(1, true, 1, 1),
(1, false, 16, 1);

INSERT INTO aggregator(version, aggregator_class, count, error_severity, login, pass, application_id) VALUES
(1, 'EmailAggregator', 5, 'ERROR', null, null, 1);

INSERT INTO email_aggregator(id, language, recipients) VALUES
(1, 'pl', '13aki13@gmail.com,buginator.noreply@gmail.com');

INSERT INTO aggregator_log(version, error_description, retry_count, status, timestamp, aggregator_id, error_id) VALUES
(1, 'Error sending email', 1, 'FAILURE', NOW(), 1, 1);