package co.salonglitt.controller;

import co.salonglitt.dto.ProveedorRequestDTO;
import co.salonglitt.dto.ProveedorResponseDTO;
import co.salonglitt.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@Tag(name = "Proveedores", description = "Proveedores de productos del salón")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    @Operation(summary = "Listar proveedores")
    public List<ProveedorResponseDTO> listar() {
        return proveedorService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proveedor por ID")
    public ProveedorResponseDTO obtener(@PathVariable Integer id) {
        return proveedorService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear proveedor")
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor")
    public ProveedorResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody ProveedorRequestDTO dto) {
        return proveedorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar proveedor")
    public void eliminar(@PathVariable Integer id) {
        proveedorService.delete(id);
    }
}