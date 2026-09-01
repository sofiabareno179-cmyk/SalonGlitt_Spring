package co.salonglitt.controller;

import co.salonglitt.dto.CatalogoPrecioRequestDTO;
import co.salonglitt.dto.CatalogoPrecioResponseDTO;
import co.salonglitt.service.CatalogoPrecioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo-precios")
@Tag(name = "Catálogo de precios", description = "Precios de los servicios por período")
public class CatalogoPrecioController {

    private final CatalogoPrecioService catalogoPrecioService;

    public CatalogoPrecioController(CatalogoPrecioService catalogoPrecioService) {
        this.catalogoPrecioService = catalogoPrecioService;
    }

    @GetMapping
    @Operation(summary = "Listar precios del catálogo")
    public List<CatalogoPrecioResponseDTO> listar() {
        return catalogoPrecioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener precio por ID")
    public CatalogoPrecioResponseDTO obtener(@PathVariable Long id) {
        return catalogoPrecioService.findById(id);
    }

    @GetMapping("/servicio/{servicioId}")
    @Operation(summary = "Listar precios por servicio")
    public List<CatalogoPrecioResponseDTO> listarPorServicio(@PathVariable Long servicioId) {
        return catalogoPrecioService.findByServicio(servicioId);
    }

    @PostMapping
    @Operation(summary = "Crear precio de catálogo", description = "El servicio debe existir")
    public ResponseEntity<CatalogoPrecioResponseDTO> crear(@Valid @RequestBody CatalogoPrecioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoPrecioService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar precio de catálogo")
    public CatalogoPrecioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody CatalogoPrecioRequestDTO dto) {
        return catalogoPrecioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar precio de catálogo")
    public void eliminar(@PathVariable Long id) {
        catalogoPrecioService.delete(id);
    }
}
