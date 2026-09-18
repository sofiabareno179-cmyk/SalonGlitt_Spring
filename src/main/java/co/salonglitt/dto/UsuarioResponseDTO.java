package co.salonglitt.dto;

public record UsuarioResponseDTO(
        Integer id,
        String nombreuser,
        String email,
        String telefono,
        String rol) {
}