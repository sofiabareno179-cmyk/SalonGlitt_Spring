package co.salonglitt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProveedorRequestDTO(
        @NotBlank(message = "El nombre de la empresa es obligatorio") String nombreEmpresa,
        @NotBlank(message = "El nombre de contacto es obligatorio") String contactoNombre,
        String telefono,
        @Email(message = "El email no es válido") String email,
        String direccion) {
}
