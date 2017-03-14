package pl.ark.chr.buginator.aggregator.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.ark.chr.buginator.TestApplicationConfiguration;
import pl.ark.chr.buginator.TestObjectCreator;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.EmailAggregator;
import pl.ark.chr.buginator.domain.Error;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-03-14.
 */
@ActiveProfiles("UNIT_TEST")
@SpringApplicationConfiguration(TestApplicationConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailAggregatorServiceImplTest {

    @Autowired
    private EmailAggregatorServiceImpl sut;

    @Autowired
    private JavaMailSender mailSender;

    @Before
    public void setUp() throws Exception {
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage(Session.getDefaultInstance(new Properties())));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNotifyExternalAggregatorInternal__Success() {
        //given
        TestObjectCreator objectCreator = new TestObjectCreator();

        String recipients = "test@test.com,test2@test.com";
        EmailAggregator aggregator = new EmailAggregator();
        aggregator.setRecipients(recipients);
        aggregator.setLanguage("pl");

        String appName = "TestApp";
        Application app = objectCreator.createApplication(null, appName);

        String errorTitle = "NullPointerException";
        Error error = new Error();
        error.setApplication(app);
        error.setTitle(errorTitle);

        doAnswer(invocationOnMock -> {
            MimeMessage msg = (MimeMessage) invocationOnMock.getArguments()[0];

            assertThat(msg.getFrom()[0].toString())
                    .isEqualTo("buginator.noreply@gmail.com");
            String receivedRecipients = Arrays.stream(msg.getRecipients(Message.RecipientType.TO))
                    .map(Address::toString)
                    .collect(Collectors.joining(","));
            assertThat(msg.getRecipients(Message.RecipientType.TO)).hasSize(2);
            assertThat(receivedRecipients)
                    .isNotNull()
                    .contains(recipients);
            assertThat(msg.getSubject())
                    .isEqualTo("Nowy błąd w systemie Buginator");

            assertThat(msg.getContent().toString())
                    .contains(appName)
                    .contains(errorTitle);

            return null;
        }).when(mailSender).send(any(MimeMessage.class));

        //when
        sut.notifyExternalAggregatorInternal(aggregator, error);

        //then
    }
}