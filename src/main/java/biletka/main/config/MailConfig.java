package biletka.main.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value(value = "${spring.mail.host}")
    private String host;
    @Value(value = "${spring.mail.username}")
    private String username;
    @Value(value = "${spring.mail.password}")
    private String password;
    @Value(value = "${spring.mail.port}")
    private int port;
    @Value(value = "${spring.mail.protocol}")
    private String protocol;
    @Value(value = "${mail.debug}")
    private String debug;
    @Value(value = "${mail.smtp.starttls.enable}")
    private String startTLS;
    @Value(value = "${mail.smtp.ssl.enable}")
    private String ssl;

    @SneakyThrows
    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        Properties properties = javaMailSender.getJavaMailProperties();

        properties.setProperty("mail.debug", debug);
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.starttls.enable", startTLS);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", ssl);

        return javaMailSender;
    }
}
