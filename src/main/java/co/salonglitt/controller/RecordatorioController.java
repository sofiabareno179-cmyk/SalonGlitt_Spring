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
@Tag(name = "Recordatorios", description = "Recordatorios asociados a los usuarios")
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
    public RecordatorioResponseDTO obtener(@PathVariable Integer id) {
        return recordatorioService.findById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar recordatorios de un usuario")
    public List<RecordatorioResponseDTO> listarPorUsuario(@PathVariable Integer usuarioId) {
        return recordatorioService.findByUsuario(usuarioId);
    }

    @PostMapping
    @Operation(summary = "Crear recordatorio", description = "El usuario debe existir")
    public ResponseEntity<RecordatorioResponseDTO> crear(@Valid @RequestBody RecordatorioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordatorioService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar recordatorio")
    public RecordatorioResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody RecordatorioRequestDTO dto) {
        return recordatorioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar recordatorio")
    public void eliminar(@PathVariable Integer id) {
        recordatorioService.delete(id);
    }
}