package co.salonglitt.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromocionRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String descripcion,
        Long servicioId,
        Long productoId,
        @NotNull(message = "El descuento es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El descuento debe ser mayor a 0")
        @DecimalMax(value = "100.0", message = "El descuento no puede superar 100%")
        BigDecimal descuento,
        @NotNull(message = "La fecha de inicio es obligatoria") LocalDate fechaInicio,
        @NotNull(message = "La fecha de fin es obligatoria") LocalDate fechaFin,
        Boolean activa) {
}
