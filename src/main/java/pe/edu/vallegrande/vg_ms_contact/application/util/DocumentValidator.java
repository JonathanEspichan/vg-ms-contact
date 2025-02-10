package pe.edu.vallegrande.vg_ms_contact.application.util;

public class DocumentValidator {
	public static boolean isValidDocument(String documentType, String documentNumber) {
		if ("DNI".equals(documentType)) {
			return documentNumber.matches("\\d{8}");
		} else if ("CNE".equals(documentType)) {
			return documentNumber.matches("\\d{15}");
		}
		return false;
	}
}
