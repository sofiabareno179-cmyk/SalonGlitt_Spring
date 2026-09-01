package co.salonglitt.controller;

import co.salonglitt.dto.RecordatorioRequestDTO;
import co.salonglitt.dto.RecordatorioResponseDTO;
import co.salonglitt.service.RecordatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recordatorios")
@Tag(name = "Recordatorios", description = "Recordatorios asociados a las citas")
public class RecordatorioController {

    private final RecordatorioService recordatorioService;

    public RecordatorioController(RecordatorioService recordatorioService) {
        this.recordatorioService = recordatorioService;
    }

    @GetMapping
    @Operation(summary = "Listar recordatorios")
    public List<RecordatorioResponseDTO> listar() {
        return recordatorioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener recordatorio por ID")
    public RecordatorioResponseDTO obtener(@PathVariable Long id) {
        return recordatorioService.findById(id);
    }

    @GetMapping("/cita/{citaId}")
    @Operation(summary = "Listar recordatorios de una cita")
    public List<RecordatorioResponseDTO> listarPorCita(@PathVariable Long citaId) {
        return recordatorioService.findByCita(citaId);
    }

    @PostMapping
    @Operation(summary = "Crear recordatorio", description = "La cita debe existir")
    public ResponseEntity<RecordatorioResponseDTO> crear(@Valid @RequestBody RecordatorioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordatorioService.create(dto));
    }

    @PatchMapping("/{id}/enviado")
    @Operation(summary = "Marcar recordatorio como enviado")
    public RecordatorioResponseDTO marcarEnviado(@PathVariable Long id) {
        return recordatorioService.marcarEnviado(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar recordatorio")
    public void eliminar(@PathVariable Long id) {
        recordatorioService.delete(id);
    }
}
