package co.salonglitt.controller;

import co.salonglitt.dto.GaleriaRequestDTO;
import co.salonglitt.dto.GaleriaResponseDTO;
import co.salonglitt.service.GaleriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/galeria")
@Tag(name = "Galería", description = "Imágenes y recursos de la galería del salón")
public class GaleriaController {

    private final GaleriaService galeriaService;

    public GaleriaController(GaleriaService galeriaService) {
        this.galeriaService = galeriaService;
    }

    @GetMapping
    @Operation(summary = "Listar galería", description = "Retorna todos los elementos. Filtra opcionalmente por tipo (imagen, video...)")
    public List<GaleriaResponseDTO> listar(@RequestParam(required = false) String tipo) {
        return tipo == null ? galeriaService.findAll() : galeriaService.findByTipo(tipo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener elemento por ID")
    public GaleriaResponseDTO obtener(@PathVariable Integer id) { return galeriaService.findById(id); }

    @PostMapping
    @Operation(summary = "Crear elemento", description = "Tipo por defecto: imagen. fechaSubida por defecto: ahora")
    public ResponseEntity<GaleriaResponseDTO> crear(@Valid @RequestBody GaleriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(galeriaService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar elemento")
    public GaleriaResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody GaleriaRequestDTO dto) {
        return galeriaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar elemento")
    public void eliminar(@PathVariable Integer id) { galeriaService.delete(id); }
}
