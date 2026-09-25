package co.salonglitt.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record ProductoProveedorRequestDTO(
        @NotNull(message = "El producto es obligatorio")
        @JsonAlias({"producto_id", "productoId"})
        Integer productoId,

        @NotNull(message = "El proveedor es obligatorio")
        @JsonAlias({"proveedor_id", "proveedorId"})
        Integer proveedorId) {
}
