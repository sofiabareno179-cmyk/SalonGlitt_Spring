package co.salonglitt.controller;

import co.salonglitt.dto.BloqueoRequestDTO;
import co.salonglitt.dto.BloqueoResponseDTO;
import co.salonglitt.service.BloqueoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bloqueos")
@Tag(name = "Bloqueos", description = "Bloqueos de disponibilidad de los estilistas")
public class BloqueoController {

    private final BloqueoService bloqueoService;

    public BloqueoController(BloqueoService bloqueoService) {
        this.bloqueoService = bloqueoService;
    }

    @GetMapping
    @Operation(summary = "Listar bloqueos")
    public List<BloqueoResponseDTO> listar() {
        return bloqueoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bloqueo por ID")
    public BloqueoResponseDTO obtener(@PathVariable Long id) {
        return bloqueoService.findById(id);
    }

    @GetMapping("/estilista/{estilistaId}")
    @Operation(summary = "Listar bloqueos por estilista")
    public List<BloqueoResponseDTO> listarPorEstilista(@PathVariable Long estilistaId) {
        return bloqueoService.findByEstilista(estilistaId);
    }

    @PostMapping
    @Operation(summary = "Crear bloqueo", description = "El estilista debe existir y la fecha de fin debe ser posterior a la de inicio")
    public ResponseEntity<BloqueoResponseDTO> crear(@Valid @RequestBody BloqueoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bloqueoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bloqueo")
    public BloqueoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody BloqueoRequestDTO dto) {
        return bloqueoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar bloqueo")
    public void eliminar(@PathVariable Long id) {
        bloqueoService.delete(id);
    }
}
