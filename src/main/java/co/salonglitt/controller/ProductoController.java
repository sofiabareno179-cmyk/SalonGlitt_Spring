package co.salonglitt.controller;

import co.salonglitt.dto.ProductoRequestDTO;
import co.salonglitt.dto.ProductoResponseDTO;
import co.salonglitt.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Productos (venta o consumo) del salón")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos")
    public List<ProductoResponseDTO> listar() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ProductoResponseDTO obtener(@PathVariable Long id) {
        return productoService.findById(id);
    }

    @GetMapping("/proveedor/{proveedorId}")
    @Operation(summary = "Listar productos por proveedor")
    public List<ProductoResponseDTO> listarPorProveedor(@PathVariable Long proveedorId) {
        return productoService.findByProveedor(proveedorId);
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "El proveedor debe existir")
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ProductoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO dto) {
        return productoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar producto")
    public void eliminar(@PathVariable Long id) {
        productoService.delete(id);
    }
}
