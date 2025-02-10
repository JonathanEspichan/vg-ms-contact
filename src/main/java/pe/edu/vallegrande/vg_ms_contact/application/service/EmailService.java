package pe.edu.vallegrande.vg_ms_contact.application.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.edu.vallegrande.vg_ms_contact.domain.model.ContactForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import pe.edu.vallegrande.vg_ms_contact.domain.model.StudyProgram;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RestTemplate restTemplate; // Para hacer la llamada HTTP al microservicio

    // Ruta base para acceder a los PDFs dentro de los recursos estáticos de Spring Boot
    private static final String PDF_RESOURCE_PATH = "src/main/resources/static/";

    // URL del microservicio de programas de estudio
    private static final String STUDY_PROGRAM_URL = "https://active-sabra-vallegrande-e443c570.koyeb.app/common/api/v1/study-program/list/active";

    // Método para enviar un correo con un archivo adjunto
    public void sendEmailWithAttachment(String to, String subject, String text, String pdfPath) {
        try {
            // Crear el mensaje MIME
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // `true` para contenido HTML
            helper.setFrom("jonathan.espichan@vallegrande.edu.pe");

            // Verificar si el archivo PDF existe antes de adjuntarlo
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                helper.addAttachment(pdfFile.getName(), pdfFile); // Adjuntar el archivo PDF
            } else {
                // Si el archivo no existe, lanzar un error
                throw new RuntimeException("El archivo PDF no fue encontrado en la ruta especificada: " + pdfPath);
            }

            // Enviar el correo
            javaMailSender.send(mimeMessage);
            System.out.println("Correo enviado con éxito a: " + to);

        } catch (MessagingException e) {
            System.err.println("Error enviando el correo: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo.", e);
        }
    }

    // Método específico para enviar el correo de confirmación con el PDF adjunto
    public void sendConfirmationEmail(ContactForm contactForm) {
        // Llamar al microservicio para obtener el nombre del programa de estudio
        String studyProgramId = contactForm.getStudyProgramId();
        String studyProgramName = obtenerNombreProgramaEstudio(studyProgramId);

        // Generar la ruta del archivo PDF
        String pdfPath = PDF_RESOURCE_PATH + studyProgramId + ".pdf";

        // Crear el mensaje de confirmación
        String subject = "INFORMACIÓN SOBRE TU PROGRAMA DE ESTUDIO: " + studyProgramName;
        String text = String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>" +
                        "  <div style='background: linear-gradient(90deg, #e0f7fa, #b3e5fc); padding: 20px; border-radius: 10px; max-width: 600px; margin: auto;'>" +
                        "    <h2 style='color: #01579b; text-align: center;'>Confirmación de formulario de contacto</h2>" +
                        "    <p style='font-size: 16px; color: #01579b;'>Hola <b>" + contactForm.getName() + " " + contactForm.getLastname_paternal() + "</b>,</p>" +
                        "    <p style='font-size: 14px; color: #01579b;'>Gracias por enviar tu formulario de contacto. Nos pondremos en contacto contigo en un instante. Hemos recibido la siguiente información:</p>" +

                        "    <div style='background: #ffffff; padding: 15px; border-radius: 8px;'>" +
                        "      <ul style='list-style-type: none; padding: 0; color: #0277bd;'>" +
                        "        <li><b>Nombre:</b> " + contactForm.getName() + " " + contactForm.getLastname_paternal() + "</li>" +
                        "        <li><b>Documento:</b> " + contactForm.getDocumentNumber() + "</li>" +
                        "        <li><b>Correo:</b> " + contactForm.getEmail() + "</li>" +
                        "        <li><b>Teléfono:</b> " + contactForm.getPhone() + "</li>" +

                        "        <li style='font-size: 20px; font-weight: bold; color: #01579b;'>" +
                        "          <b>¡Has elegido el programa de estudio: <span style='color: #ff7043;'> " + studyProgramName + "</span>!</b>" +
                        "        </li>" +
                        "      </ul>" +
                        "    </div>" +

                        "    <p style='margin-top: 20px; color: #01579b;'>Recuerda que puedes DESCARGAR el PDF del PROGRAMA DE ESTUDIO adjunto a este correo.</p>" +
                        "    <p style='margin-top: 20px; text-align: center; color: #01579b; font-size: 16px; font-weight: bold;'>Nos pondremos en contacto contigo en un instante.</p>" +

                        "    <p style='margin-top: 20px; text-align: center; color: #01579b;'>Saludos,<br><b>El equipo de soporte del CETPRO</b></p>" +
                        "  </div>" +
                        "</body>" +
                        "</html>",
                contactForm.getName(),
                contactForm.getLastname_paternal(),
                contactForm.getName(),
                contactForm.getLastname_paternal(),
                contactForm.getDocumentNumber(),
                contactForm.getEmail(),
                contactForm.getPhone(),
                studyProgramName,
                contactForm.getDescription()
        );

        // Enviar el correo con el PDF adjunto
        sendEmailWithAttachment(contactForm.getEmail(), subject, text, pdfPath);
    }

    // Método para obtener el nombre del programa de estudio desde el microservicio
    public String obtenerNombreProgramaEstudio(String programId) {
        try {
            // URL de la API que devuelve los programas de estudio activos
            String url = "https://active-sabra-vallegrande-e443c570.koyeb.app/common/api/v1/study-program/list/active";

            // Realizamos la petición GET para obtener el listado de programas
            ResponseEntity<StudyProgram[]> response = restTemplate.getForEntity(url, StudyProgram[].class);

            // Iterar sobre los programas para buscar el que coincida con el ID
            for (StudyProgram program : response.getBody()) {
                if (program.getProgramId().equals(programId)) {
                    return program.getName();
                }
            }
            return "Programa no encontrado";
        } catch (HttpClientErrorException e) {
            // Manejo de errores en caso de fallo en la solicitud
            System.err.println("Error al obtener el programa de estudio: " + e.getMessage());
            return "Error al obtener el programa";
        }
    }

}
