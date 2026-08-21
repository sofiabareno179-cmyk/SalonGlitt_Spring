package co.salonglitt.controller;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@Tag(name = "Agendas", description = "Bloques de disponibilidad de los estilistas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    @Operation(summary = "Listar bloques de agenda", description = "Retorna todos los bloques con su estilista")
    public List<AgendaResponseDTO> listar() { return agendaService.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bloque de agenda por ID")
    public AgendaResponseDTO obtener(@PathVariable Long id) { return agendaService.findById(id); }

    @GetMapping("/estilista/{estilistaId}")
    @Operation(summary = "Listar agenda por estilista", description = "Filtra opcionalmente por fecha (yyyy-MM-dd)")
    public List<AgendaResponseDTO> listarPorEstilista(
            @PathVariable Long estilistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return fecha == null
                ? agendaService.findByEstilista(estilistaId)
                : agendaService.findByEstilistaYFecha(estilistaId, fecha);
    }

    @PostMapping
    @Operation(summary = "Crear bloque de agenda", description = "El estilista debe existir y la hora fin debe ser posterior a la inicio")
    public ResponseEntity<AgendaResponseDTO> crear(@Valid @RequestBody AgendaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bloque de agenda")
    public AgendaResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody AgendaRequestDTO dto) {
        return agendaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar bloque de agenda")
    public void eliminar(@PathVariable Long id) { agendaService.delete(id); }
}