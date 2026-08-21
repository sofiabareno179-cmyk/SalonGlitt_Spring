package co.salonglitt.controller;

import co.salonglitt.dto.ServicioRequestDTO;
import co.salonglitt.dto.ServicioResponseDTO;
import co.salonglitt.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@Tag(name = "Servicios", description = "Servicios ofrecidos por el salón (corte, tinte, uñas...)")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    @Operation(summary = "Listar servicios", description = "Retorna todos los servicios con precio y duración")
    public List<ServicioResponseDTO> listar() { return servicioService.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID")
    public ServicioResponseDTO obtener(@PathVariable Long id) { return servicioService.findById(id); }

    @PostMapping
    @Operation(summary = "Crear servicio")
    public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio")
    public ServicioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ServicioRequestDTO dto) {
        return servicioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar servicio")
    public void eliminar(@PathVariable Long id) { servicioService.delete(id); }
}