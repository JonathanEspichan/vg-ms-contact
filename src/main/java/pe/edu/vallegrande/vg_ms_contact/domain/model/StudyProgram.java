package pe.edu.vallegrande.vg_ms_contact.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "study-program")
public class StudyProgram {

    @Id
    private String programId;
    private String name;
    private String module;
    private String trainingLevel;
    private String studyPlanType;
    private String credits;
    private String hours;
    private String status = "A" ;
    private String cetproId;

}
