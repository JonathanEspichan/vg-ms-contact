package pe.edu.vallegrande.vg_ms_contact.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "contact_form")
public class ContactForm {
    @Id
    private String id;
    private String name;
    private String lastname_maternal;
    private String lastname_paternal;
    private String birthdate;
    private String document_type;
    private String documentNumber;
    private String email;
    private String phone;
    private String phone_optional;
    private String studyProgramId;
    private String description;
    private String status = "A";
    
}
