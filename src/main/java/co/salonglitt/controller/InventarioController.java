package co.salonglitt.controller;

import co.salonglitt.dto.InventarioRequestDTO;
import co.salonglitt.dto.InventarioResponseDTO;
import co.salonglitt.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Control de stock de productos")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    @Operation(summary = "Listar inventario")
    public List<InventarioResponseDTO> listar() {
        return inventarioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inventario por ID")
    public InventarioResponseDTO obtener(@PathVariable Long id) {
        return inventarioService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear inventario", description = "El producto debe existir y no tener inventario previo")
    public ResponseEntity<InventarioResponseDTO> crear(@Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inventario")
    public InventarioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody InventarioRequestDTO dto) {
        return inventarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar inventario")
    public void eliminar(@PathVariable Long id) {
        inventarioService.delete(id);
    }
}
