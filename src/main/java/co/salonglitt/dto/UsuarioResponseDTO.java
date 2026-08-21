package co.salonglitt.dto;

public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String email,
        String telefono,
        Long perfilId,
        String perfilNombre,
        boolean activo) {
}