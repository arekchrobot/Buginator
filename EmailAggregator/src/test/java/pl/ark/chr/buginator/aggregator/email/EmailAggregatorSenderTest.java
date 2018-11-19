package pl.ark.chr.buginator.aggregator.email;

import freemarker.template.Configuration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.commons.util.NetworkUtil;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailAggregatorSenderTest {

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Mock
    private Configuration freeMarkerConfig;
    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private MessageSource aggregatorEmailMessageSource;

    @Spy
    @InjectMocks
    private EmailAggregatorSender emailAggregatorSender;

    @Before
    public void setUp() throws Exception {
        doReturn("").when(emailAggregatorSender).constructEmailBody(anyMap());
        doReturn("BASIC_TOPIC").when(aggregatorEmailMessageSource)
                .getMessage(eq("errorNotification.topic"), isNull(), any(Locale.class));
        doReturn("BASIC_TITLE").when(aggregatorEmailMessageSource)
                .getMessage(eq("errorNotification.title"), isNull(), any(Locale.class));
        NetworkUtil.setHostIP("127.0.0.1");
        NetworkUtil.setHostPort(8080);
        ReflectionTestUtils.setField(emailAggregatorSender, "mailQueue", "mailQueue");
    }

    @Test
    public void shouldBeValidStrategy() {
        assertThat(emailAggregatorSender.isValid(EmailAggregator.EMAIL_AGGREGATOR_NAME)).isTrue();
    }

    @Test
    public void shouldCreateCorrectTemplateModel() {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = TestObjectCreator.createTestError(app);
        var locale = new Locale("en");

        //when
        Map<String, Object> result = emailAggregatorSender.createTemplateModel(error, locale);

        //then
        assertThat(result.keySet())
                .hasSize(4)
                .containsOnly("errorTitle", "applicationName", "errorUrl", "topic");
        assertThat(result.values())
                .hasSize(4)
                .containsOnly(error.getTitle(), app.getName(), "http://127.0.0.1:8080/application/1/error/2", "BASIC_TOPIC");
    }

    @Test
    public void shouldCreateTemplateModelAndFillNull() {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = Error.builder(null, ErrorSeverity.ERROR, ErrorStatus.CREATED,
                "2018-11-11 11:11:11", app)
                .build();
        var locale = new Locale("en");

        //when
        Map<String, Object> result = emailAggregatorSender.createTemplateModel(error, locale);

        //then
        assertThat(result.keySet())
                .hasSize(4)
                .containsOnly("errorTitle", "applicationName", "errorUrl", "topic");
        assertThat(result.values())
                .hasSize(4)
                .containsOnly(null, app.getName(), "http://127.0.0.1:8080/application/1/error/null", "BASIC_TOPIC");
    }

    @Test
    public void shouldCreateUrlWithoutPort() {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = TestObjectCreator.createTestError(app);
        error.setId(2L);
        NetworkUtil.setHostIP("buginator.com");
        NetworkUtil.setHostPort(0);

        //when
        String errorUrl = emailAggregatorSender.createUrl(error);

        //then
        assertThat(errorUrl)
                .isNotEmpty()
                .isEqualTo("http://buginator.com/application/1/error/2");
    }

    @Test
    public void shouldConstructProperEmailDTO() {
        //given
        var app = TestObjectCreator.createTestApplication();
        var emailAggregator = TestObjectCreator.getPrecofiguredEmailAggregatorBuilder(app).build();
        var locale = new Locale("en");

        //when
        EmailDTO result = emailAggregatorSender.constructEmail(emailAggregator, locale, "testMessageBody");

        //then
        assertThat(result.getFrom()).isEqualTo(emailAggregator.getLogin());
        assertThat(result.getTo()).isEqualTo(emailAggregator.getRecipients());
        assertThat(result.getCc()).isEqualTo(emailAggregator.getCc());
        assertThat(result.getBcc()).isNullOrEmpty();
        assertThat(result.isSsl()).isFalse();
        assertThat(result.isHtmlBody()).isTrue();
        assertThat(result.getSubject()).isEqualTo("BASIC_TITLE");
        assertThat(result.getEmailBody()).isEqualTo("testMessageBody");
    }

    @Test
    public void shouldSendDataToJmsQueue() throws Exception {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = TestObjectCreator.createTestError(app);
        var emailAggregator = TestObjectCreator.getPrecofiguredEmailAggregatorBuilder(app).build();

        //when
        emailAggregatorSender.sendData(emailAggregator, error);

        //then
        verify(emailAggregatorSender).constructEmailBody(anyMap());
        verify(jmsTemplate).convertAndSend(anyString(), any(EmailDTO.class));
    }

    @Test
    public void shouldThrowExceptionWhenUnableToCreateEmailBody() throws Exception {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = TestObjectCreator.createTestError(app);
        var emailAggregator = TestObjectCreator.getPrecofiguredEmailAggregatorBuilder(app).build();

        doThrow(new IOException("Error")).when(emailAggregatorSender).constructEmailBody(anyMap());

        fluentThrown.expect(EmailCreationException.class)
                .hasMessage("Error creating email body template for error: 2")
                .hasCauseInstanceOf(IOException.class);

        //when
        emailAggregatorSender.sendData(emailAggregator, error);
    }
}