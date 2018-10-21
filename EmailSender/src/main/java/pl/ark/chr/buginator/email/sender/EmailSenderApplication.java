package pl.ark.chr.buginator.email.sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class EmailSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailSenderApplication.class, args);
    }
}
