package co.salonglitt.dto;

public record PerfilResponseDTO(
        Integer id,
        String nombre,
        String apellido,
        String bio,
        Integer idusuario,
        String usuarioNombre) {
}