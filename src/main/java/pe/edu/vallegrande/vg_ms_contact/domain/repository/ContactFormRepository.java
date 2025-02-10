package pe.edu.vallegrande.vg_ms_contact.domain.repository;

import pe.edu.vallegrande.vg_ms_contact.domain.model.ContactForm;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ContactFormRepository extends ReactiveMongoRepository<ContactForm, String> {
    Flux<ContactForm> findByStatus(String status);
    Mono<ContactForm> findByPhone(String phone); 
    Mono<ContactForm> findByDocumentNumber(String documentNumber); // nuevo método
}
