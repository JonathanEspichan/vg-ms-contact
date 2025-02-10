package pe.edu.vallegrande.vg_ms_contact.presentation.controller;

import pe.edu.vallegrande.vg_ms_contact.domain.dto.ContactFormDTO;
import pe.edu.vallegrande.vg_ms_contact.domain.model.ContactForm;
import pe.edu.vallegrande.vg_ms_contact.application.service.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/management/${api.version}/contacts")
public class ContactFormController {

	@Autowired
	private ContactFormService contactFormService;

	@GetMapping
	public Flux<ContactForm> getAllUsers() {
		return contactFormService.getAllUsers();
	}

	@GetMapping("/{id}")
	public Mono<ContactForm> getUserById(@PathVariable String id) {
		return contactFormService.getUserById(id);
	}

	@PostMapping
	public Mono<ContactForm> createUser(@RequestBody ContactFormDTO contactFormDTO) {
		return contactFormService.createUser(contactFormDTO);
	}

	@PutMapping("/{id}")
	public Mono<ContactForm> updateUser(@PathVariable String id, @RequestBody ContactFormDTO contactFormDTO) {
		return contactFormService.updateUser(id, contactFormDTO);
	}

	@PutMapping("/reactivate/{id}")
	public Mono<ContactForm> reactivateUser(@PathVariable String id) {
		return contactFormService.reactivateUser(id);
	}

	@DeleteMapping("/{id}")
	public Mono<Void> deleteUser(@PathVariable String id) {
		return contactFormService.deleteUser(id);
	}

	@DeleteMapping("/fisico/{id}")
	public Mono<String> deleteUserPhysically(@PathVariable String id) {
		return contactFormService.deleteUserPhysically(id);
	}

	@GetMapping("/active")
	public Flux<ContactForm> getActiveUsers() {
		return contactFormService.getActiveUsers();
	}

	@GetMapping("/inactive")
	public Flux<ContactForm> getInactiveUsers() {
		return contactFormService.getInactiveUsers();
	}
	
	@GetMapping("/buscarPorCelular/{phone}")
    public Mono<ContactForm> getUserByPhone(@PathVariable String phone) {
        return contactFormService.getUserByPhone(phone);
    }
	
	@GetMapping("/buscarPorDocumento/{documentNumber}")
    public Mono<ContactForm> getUserByDocumentNumber(@PathVariable String documentNumber) {
        return contactFormService.getUserByDocumentNumber(documentNumber);
    }
	
	
}