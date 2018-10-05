package pl.ark.chr.buginator.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.TestObjectCreator;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.ErrorRepository;
import pl.ark.chr.buginator.service.ErrorService;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-01-29.
 */
@RunWith(MockitoJUnitRunner.class)
public class ErrorServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";

    @InjectMocks
    private ErrorService sut = new ErrorServiceImpl();

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private MessageSource messageSource;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
//        when(messageSource.getMessage(any(String.class), nullable(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetAllByApplication() throws Exception {
        //given
        String email = "test@test.test";
        User user = TestObjectCreator.createUser(email, null, null, "");

        String appName = "Test App";
        Application app = new Application();
        app.setId(1L);
        app.setName(appName);

        UserApplication ua = TestObjectCreator.createUserApplication(user, app, true);

        Set<UserApplication> permissions = new HashSet<>();
        permissions.add(ua);

        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(app));
        when(errorRepository.findByApplication(any(Application.class))).thenReturn(TestObjectCreator.generateErrorListForSorting(app));

        //when
        List<Error> result = sut.getAllByApplication(1L, permissions);

        //then
        assertThat(result)
                .extracting(Error::getId)
                .containsExactly(TestObjectCreator.getSortedErrorIdArray());
    }

}