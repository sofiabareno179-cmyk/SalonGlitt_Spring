package co.salonglitt.controller;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "Citas agendadas por los usuarios")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    @Operation(summary = "Listar citas", description = "Retorna todas las citas. Filtra opcionalmente por estado")
    public List<CitaResponseDTO> listar(@RequestParam(required = false) String estado) {
        return estado == null ? citaService.findAll() : citaService.findByEstado(estado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID")
    public CitaResponseDTO obtener(@PathVariable Integer id) { return citaService.findById(id); }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar citas por usuario")
    public List<CitaResponseDTO> listarPorUsuario(@PathVariable Integer usuarioId) {
        return citaService.findByUsuario(usuarioId);
    }

    @PostMapping
    @Operation(summary = "Crear cita", description = "El usuario debe existir. Estado por defecto: Espera")
    public ResponseEntity<CitaResponseDTO> crear(@Valid @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.create(dto));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de la cita", description = "Ej. Confirmada, Espera, Cancelada")
    public CitaResponseDTO cambiarEstado(@PathVariable Integer id, @RequestParam String estado) {
        return citaService.cambiarEstado(id, estado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita")
    public CitaResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody CitaRequestDTO dto) {
        return citaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar cita")
    public void eliminar(@PathVariable Integer id) { citaService.delete(id); }
}