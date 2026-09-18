package co.salonglitt.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record ServicioProductoRequestDTO(
        @NotNull(message = "El servicio es obligatorio")
        @JsonAlias({"servicio_id", "servicioId"})
        Integer servicioId,

        @NotNull(message = "El producto es obligatorio")
        @JsonAlias({"producto_id", "productoId"})
        Integer productoId) {
}
