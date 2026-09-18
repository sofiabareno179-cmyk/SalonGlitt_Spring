package co.salonglitt.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecordatorioRequestDTO(
        @NotBlank(message = "El título es obligatorio")
        String titulo,

        String mensaje,

        @NotBlank(message = "La fecha del recordatorio es obligatoria")
        @JsonAlias({"fecha_recordatorio", "fechaRecordatorio"})
        String fecha_recordatorio,

        @NotNull(message = "El usuario es obligatorio")
        Integer idusuario) {
}