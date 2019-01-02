package pl.ark.chr.buginator.aggregator.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.commons.util.NetworkUtil;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationContext.class)
public class EmailAggregatorSenderIT {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.queue.mailQueue}")
    private String mailQueue;

    @Autowired
    private EmailAggregatorSender emailAggregatorSender;

    String EXPECTED_BODY_EN = "<html>\n" +
            "    <body>\n" +
            "        <p style=\"margin:15px 0;\">\n" +
            "            <b>New error in application: app</b>\n" +
            "        </p>\n" +
            "        <p style=\"margin:15px 0;\"> error</p>\n" +
            "        <p style=\"margin:15px 0;\"> http://buginator.com/application/1/error/3</p>\n" +
            "        Buginator.com</p>\n" +
            "    </body>\n" +
            "</html>";

    String EXPECTED_BODY_PL = "<html>\n" +
            "    <body>\n" +
            "        <p style=\"margin:15px 0;\">\n" +
            "            <b>Nowy błąd w aplikacji: app</b>\n" +
            "        </p>\n" +
            "        <p style=\"margin:15px 0;\"> error</p>\n" +
            "        <p style=\"margin:15px 0;\"> http://buginator.com/application/1/error/2</p>\n" +
            "        Buginator.com</p>\n" +
            "    </body>\n" +
            "</html>";

    @BeforeEach
    public void setUp() throws Exception {
        NetworkUtil.setHostIP("buginator.com");
        jmsTemplate.setReceiveTimeout(10_000);
    }

    @Test
    @DisplayName("should build proper email and send it to jms queue")
    public void shouldCreateCorrectEnEmailAndSendToJms() {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = TestObjectCreator.createTestError(app);
        error.setId(3L);
        var emailAggregator = TestObjectCreator.getPrecofiguredEmailAggregatorBuilder(app).build();

        //when
        emailAggregatorSender.sendData(emailAggregator, error);

        //then
        EmailDTO result = (EmailDTO) jmsTemplate.receiveAndConvert(mailQueue);
        assertThat(result).isNotNull();
        assertThat(result.getEmailBody()).isEqualTo(EXPECTED_BODY_EN);
        assertThat(result.getSubject()).isEqualTo("New error in Buginator");
    }

    @Test
    @DisplayName("should use correct locale for creating email and send it to jms queue")
    public void shouldCreateCorrectPlEmailAndSendToJms() {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = TestObjectCreator.createTestError(app);
        var emailAggregator = TestObjectCreator.getPrecofiguredEmailAggregatorBuilder(app)
                .language("pl")
                .build();

        //when
        emailAggregatorSender.sendData(emailAggregator, error);

        //then
        EmailDTO result = (EmailDTO) jmsTemplate.receiveAndConvert(mailQueue);
        assertThat(result).isNotNull();
        assertThat(result.getEmailBody()).isEqualTo(EXPECTED_BODY_PL);
        assertThat(result.getSubject()).isEqualTo("Nowy błąd w systemie Buginator");
    }

    @Test
    @DisplayName("should use default EN locale when passing not supported lang")
    public void shouldUseEnWhenLangNotSupported() {
        //given
        var app = TestObjectCreator.createTestApplication();
        var error = TestObjectCreator.createTestError(app);
        error.setId(3L);
        var emailAggregator = TestObjectCreator.getPrecofiguredEmailAggregatorBuilder(app)
                .language("ru")
                .build();

        //when
        emailAggregatorSender.sendData(emailAggregator, error);

        //then
        EmailDTO result = (EmailDTO) jmsTemplate.receiveAndConvert(mailQueue);
        assertThat(result).isNotNull();
        assertThat(result.getEmailBody()).isEqualTo(EXPECTED_BODY_EN);
        assertThat(result.getSubject()).isEqualTo("New error in Buginator");
    }
}
