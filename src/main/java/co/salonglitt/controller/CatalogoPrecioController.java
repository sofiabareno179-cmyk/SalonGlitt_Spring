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
@Tag(name = "Catálogo de precios", description = "Precios de los servicios por categoría")
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
    public CatalogoPrecioResponseDTO obtener(@PathVariable Integer id) {
        return catalogoPrecioService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear precio de catálogo")
    public ResponseEntity<CatalogoPrecioResponseDTO> crear(@Valid @RequestBody CatalogoPrecioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoPrecioService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar precio de catálogo")
    public CatalogoPrecioResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody CatalogoPrecioRequestDTO dto) {
        return catalogoPrecioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar precio de catálogo")
    public void eliminar(@PathVariable Integer id) {
        catalogoPrecioService.delete(id);
    }
}