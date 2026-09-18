package co.salonglitt.dto;

public record ProductoProveedorResponseDTO(
        Integer producto_id,
        Integer proveedor_id,
        String nombre_producto,
        String nombre_proveedor) {
}
