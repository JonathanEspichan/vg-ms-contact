package pe.edu.vallegrande.vg_ms_contact.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configuración del servidor SMTP
        mailSender.setHost("smtp.gmail.com");  // Servidor SMTP de Gmail
        mailSender.setPort(587);  // Puerto de Gmail (587 es para TLS)

        // Configuración de la cuenta de correo
        mailSender.setUsername("jonathan.espichan@vallegrande.edu.pe");
        mailSender.setPassword("pfgdscjcyizsjotm");

        // Propiedades para el correo (habilitar TLS)
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }

    // Si necesitas personalizar más, puedes agregar configuraciones adicionales aquí.
}
