package co.salonglitt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProveedorRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String telefono,
        @Email(message = "El email no es válido") String email,
        String direccion,
        Boolean activo) {
}
