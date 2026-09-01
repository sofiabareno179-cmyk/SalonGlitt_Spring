package co.salonglitt.controller;

import co.salonglitt.dto.PromocionRequestDTO;
import co.salonglitt.dto.PromocionResponseDTO;
import co.salonglitt.service.PromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@Tag(name = "Promociones", description = "Descuentos sobre servicios o productos")
public class PromocionController {

    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping
    @Operation(summary = "Listar promociones")
    public List<PromocionResponseDTO> listar() {
        return promocionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener promoción por ID")
    public PromocionResponseDTO obtener(@PathVariable Long id) {
        return promocionService.findById(id);
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar promociones activas")
    public List<PromocionResponseDTO> listarActivas() {
        return promocionService.findActivas();
    }

    @PostMapping
    @Operation(summary = "Crear promoción", description = "Debe aplicar a un servicio o a un producto")
    public ResponseEntity<PromocionResponseDTO> crear(@Valid @RequestBody PromocionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promocionService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar promoción")
    public PromocionResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody PromocionRequestDTO dto) {
        return promocionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar promoción")
    public void eliminar(@PathVariable Long id) {
        promocionService.delete(id);
    }
}
