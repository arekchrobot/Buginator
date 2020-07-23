INSERT INTO buginator_error_stack_trace(version, stack_trace, stack_trace_order, error_id) VALUES
    (1, 'Exception: java.lang.NullPointerException', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at java.base/java.util.List.copyOf(List.java:1061)', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.domain.core.Error.getStackTrace(Error.java:114)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorDetailsMapperImpl.toDto(ErrorDetailsMapperImpl.java:47)', 5, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 6, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)', 7, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'Caused by: java.lang.NullPointerException', 8, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 9, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),
    (1, 'at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)', 10, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'CREATED')),

    (1, 'Exception: Invalid payment', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at java.base/java.util.List.copyOf(List.java:1061)', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at pl.ark.chr.buginator.domain.core.Error.getStackTrace(Error.java:114)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorDetailsMapperImpl.toDto(ErrorDetailsMapperImpl.java:47)', 5, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 6, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)', 7, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'Caused by: Invalid payment', 8, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 9, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),
    (1, 'at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)', 10, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '3 days'::interval
                                                           AND title = 'Invalid payment' AND status = 'ONGOING')),

    (1, 'Exception: Access denied', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Access denied' AND status = 'CREATED')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Access denied' AND status = 'CREATED')),
    (1, 'Caused by: Access denied', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Access denied' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Access denied' AND status = 'CREATED')),

    (1, 'Exception: Fatal exception', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '2 months'::interval
                                                           AND title = 'Fatal exception' AND status = 'REOPENED')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '2 months'::interval
                                                           AND title = 'Fatal exception' AND status = 'REOPENED')),
    (1, 'Caused by: Fatal exception', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '2 months'::interval
                                                           AND title = 'Fatal exception' AND status = 'REOPENED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '2 months'::interval
                                                           AND title = 'Fatal exception' AND status = 'REOPENED')),

    (1, 'Exception: StackOverflowException', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '1 month'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '1 month'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),
    (1, 'Caused by: StackOverflowException', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '1 month'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '1 month'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),

    (1, 'Exception: StackOverflowException', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '17 days'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '17 days'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),
    (1, 'Caused by: StackOverflowException', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '17 days'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '17 days'::interval
                                                           AND title = 'StackOverflowException' AND status = 'CREATED')),

    (1, 'Exception: OutOfMemoryException', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '6 days'::interval
                                                           AND title = 'OutOfMemoryException' AND status = 'CREATED')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '6 days'::interval
                                                           AND title = 'OutOfMemoryException' AND status = 'CREATED')),

    (1, 'Exception: EmailNotSentException', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '2 days'::interval
                                                           AND title = 'EmailNotSentException' AND status = 'REOPENED')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '2 days'::interval
                                                           AND title = 'EmailNotSentException' AND status = 'REOPENED')),

    (1, 'Exception: DataNotFound', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '1 month'::interval
                                                           AND title = 'DataNotFound' AND status = 'ONGOING')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '1 month'::interval
                                                           AND title = 'DataNotFound' AND status = 'ONGOING')),

    (1, 'Exception: Internal error', 1, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Internal error' AND status = 'ONGOING')),
    (1, 'at java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)', 2, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Internal error' AND status = 'ONGOING')),

    (1, 'Caused by: Payment unsuccessful', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Payment unsuccessful' AND status = 'CREATED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'Payment unsuccessful' AND status = 'CREATED')),

    (1, 'Caused by: NullPointerException', 3, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'RESOLVED')),
    (1, 'at pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)', 4, (SELECT id FROM buginator_error
                                                         WHERE last_occurrence = current_date - '10 days'::interval
                                                           AND title = 'NullPointerException' AND status = 'RESOLVED'));