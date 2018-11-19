package pl.ark.chr.buginator.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.ark.chr.buginator.TestApplicationConfiguration;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.User;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-11-27.
 */
@ActiveProfiles("UNIT_TEST")
@SpringBootTest(classes = TestApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class EmailServiceImplTest {

    @Autowired
    private EmailServiceImpl sut;

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
    public void testSendRegister__Success() {
        //given
        Company company = new Company("Test Company", new PaymentOption());
        company.setId(1L);
        company.setName("Test Company");
//        company.setToken("TOKEN");
//        company.setUniqueKey("UNIQUE");

        String toMail = "test@gmail.com";

        doAnswer(invocationOnMock -> {
            MimeMessage msg = (MimeMessage) invocationOnMock.getArguments()[0];

            assertThat(msg.getFrom()[0].toString())
                    .isEqualTo("buginator.app.noreply@gmail.com");
            assertThat(msg.getRecipients(Message.RecipientType.TO)[0].toString())
                    .isEqualTo(toMail);
            assertThat(msg.getSubject())
                    .isEqualTo("Thank you for registering to Buginator");

            //TODO: fix assert after template is back
//            assertThat(msg.getContent().toString())
//                    .contains(company.getName())
//                    .contains(company.getToken())
//                    .contains(company.getUniqueKey());
            return null;
        }).when(mailSender).send(any(MimeMessage.class));

        //when
        sut.sendRegister(company, new Locale("en"), toMail);

        //then
    }

    @Test
    public void testSendResetPassword__Success() {
        //given
        String newPassword = "test password";
        String username = "Test User";
        String email = "testEmail@gmail.com";

        User user = new User.Builder().build();
        user.setName(username);
        user.setEmail(email);


        doAnswer(invocationOnMock -> {
            MimeMessage msg = (MimeMessage) invocationOnMock.getArguments()[0];

            assertThat(msg.getFrom()[0].toString())
                    .isEqualTo("buginator.app.noreply@gmail.com");
            assertThat(msg.getRecipients(Message.RecipientType.TO)[0].toString())
                    .isEqualTo(email);
            assertThat(msg.getSubject())
                    .isEqualTo("Twoje hasło zostało zresetowane");

            //TODO: fix assert after template is back
//            assertThat(msg.getContent().toString())
//                    .contains(username)
//                    .contains(newPassword);
            return null;
        }).when(mailSender).send(any(MimeMessage.class));

        //when
        sut.sendResetPassword(user, new Locale("pl"), newPassword);

        //then
    }
}