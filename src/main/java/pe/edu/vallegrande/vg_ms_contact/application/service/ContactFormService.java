package pe.edu.vallegrande.vg_ms_contact.application.service;

import pe.edu.vallegrande.vg_ms_contact.domain.dto.ContactFormDTO;
import pe.edu.vallegrande.vg_ms_contact.domain.model.ContactForm;
import pe.edu.vallegrande.vg_ms_contact.domain.repository.ContactFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ContactFormService {

	@Autowired
	private ContactFormRepository contactFormRepository;

	@Autowired
	private EmailService emailService; // Inyección del servicio de correo

	public Flux<ContactForm> getAllUsers() {
		return contactFormRepository.findAll();
	}

	public Mono<ContactForm> getUserById(String id) {
		return contactFormRepository.findById(id);
	}

	public Mono<ContactForm> createUser(ContactFormDTO contactFormDTO) {
		ContactForm contactForm = new ContactForm();
		contactForm.setName(contactFormDTO.getName());
		contactForm.setLastname_maternal(contactFormDTO.getLastname_maternal());
		contactForm.setLastname_paternal(contactFormDTO.getLastname_paternal());
		contactForm.setBirthdate(contactFormDTO.getBirthdate());
		contactForm.setDocument_type(contactFormDTO.getDocument_type());
		contactForm.setDocumentNumber(contactFormDTO.getDocumentNumber());
		contactForm.setEmail(contactFormDTO.getEmail());
		contactForm.setPhone(contactFormDTO.getPhone());
		contactForm.setPhone_optional(contactFormDTO.getPhone_optional());
		contactForm.setStudyProgramId(contactFormDTO.getStudyProgramId());
		contactForm.setDescription(contactFormDTO.getDescription());
		contactForm.setStatus("A");

		// Guardar el formulario de contacto en la base de datos
		return contactFormRepository.save(contactForm)
				.flatMap(savedContactForm -> {
					// Enviar correo de confirmación después de guardar el contacto
					emailService.sendConfirmationEmail(savedContactForm);  // Llamada al nuevo método
					return Mono.just(savedContactForm);  // Retornamos el formulario después de enviar el correo
				});
	}


	public Mono<ContactForm> updateUser(String id, ContactFormDTO contactFormDTO) {
		return contactFormRepository.findById(id).flatMap(user -> {
			user.setName(contactFormDTO.getName());
			user.setLastname_maternal(contactFormDTO.getLastname_maternal());
			user.setLastname_paternal(contactFormDTO.getLastname_paternal());
			user.setBirthdate(contactFormDTO.getBirthdate());
			user.setDocument_type(contactFormDTO.getDocument_type());
			user.setDocumentNumber(contactFormDTO.getDocumentNumber());
			user.setEmail(contactFormDTO.getEmail());
			user.setPhone(contactFormDTO.getPhone());
			user.setPhone_optional(contactFormDTO.getPhone_optional());
			user.setStudyProgramId(contactFormDTO.getStudyProgramId());
			user.setDescription(contactFormDTO.getDescription());
			return contactFormRepository.save(user);
		});
	}

	public Mono<Void> deleteUser(String id) {
		return contactFormRepository.findById(id).flatMap(user -> {
			user.setStatus("I");
			return contactFormRepository.save(user).then(Mono.empty());
		});
	}

	public Mono<ContactForm> reactivateUser(String id) {
		return contactFormRepository.findById(id).flatMap(user -> {
			if (user != null && user.getStatus().equals("I")) {
				user.setStatus("A");
				return contactFormRepository.save(user);
			} else {
				return Mono.error(new IllegalArgumentException("El usuario no está inactivo o no existe"));
			}
		});
	}

	public Mono<String> deleteUserPhysically(String id) {
		return contactFormRepository.deleteById(id).then(Mono.just("MUY BIEN REGISTRO ELIMINADO PERMANENTEMENTE"));
	}

	public Flux<ContactForm> getActiveUsers() {
		return contactFormRepository.findByStatus("A");
	}

	public Flux<ContactForm> getInactiveUsers() {
		return contactFormRepository.findByStatus("I");
	}

	public Mono<ContactForm> getUserByPhone(String phone) {
		return contactFormRepository.findByPhone(phone);
	}

	public Mono<ContactForm> getUserByDocumentNumber(String documentNumber) {
		return contactFormRepository.findByDocumentNumber(documentNumber);
	}
}
