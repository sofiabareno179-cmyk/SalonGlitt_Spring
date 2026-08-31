package co.salonglitt.controller;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@Tag(name = "Agendas", description = "Horarios semanales de disponibilidad de cada usuario")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    @Operation(summary = "Listar agendas", description = "Retorna todos los horarios semanales")
    public List<AgendaResponseDTO> listar() { return agendaService.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener agenda por ID")
    public AgendaResponseDTO obtener(@PathVariable Integer id) { return agendaService.findById(id); }

    @PostMapping
    @Operation(summary = "Crear horario", description = "El usuario debe existir. Ej. diasemana=lunes, horainicio=09:00, horafin=14:00")
    public ResponseEntity<AgendaResponseDTO> crear(@Valid @RequestBody AgendaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar horario")
    public AgendaResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody AgendaRequestDTO dto) {
        return agendaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar horario")
    public void eliminar(@PathVariable Integer id) { agendaService.delete(id); }
}
