package co.salonglitt.controller;

import co.salonglitt.dto.ProductoProveedorRequestDTO;
import co.salonglitt.dto.ProductoProveedorResponseDTO;
import co.salonglitt.service.ProductoProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto-proveedores")
@Tag(name = "Producto-Proveedor", description = "Relación N:N entre productos y proveedores")
public class ProductoProveedorController {

    private final ProductoProveedorService productoProveedorService;

    public ProductoProveedorController(ProductoProveedorService productoProveedorService) {
        this.productoProveedorService = productoProveedorService;
    }

    @GetMapping
    @Operation(summary = "Listar relaciones", description = "Filtra opcionalmente por producto o proveedor")
    public List<ProductoProveedorResponseDTO> listar(@RequestParam(required = false) Integer productoId,
                                                     @RequestParam(required = false) Integer proveedorId) {
        if (productoId != null) return productoProveedorService.findByProducto(productoId);
        if (proveedorId != null) return productoProveedorService.findByProveedor(proveedorId);
        return productoProveedorService.findAll();
    }

    @GetMapping("/{productoId}/{proveedorId}")
    @Operation(summary = "Obtener relación por producto y proveedor")
    public ProductoProveedorResponseDTO obtener(@PathVariable Integer productoId, @PathVariable Integer proveedorId) {
        return productoProveedorService.findById(productoId, proveedorId);
    }

    @PostMapping
    @Operation(summary = "Crear relación producto-proveedor")
    public ResponseEntity<ProductoProveedorResponseDTO> crear(@Valid @RequestBody ProductoProveedorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoProveedorService.create(dto));
    }

    @DeleteMapping("/{productoId}/{proveedorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar relación producto-proveedor")
    public void eliminar(@PathVariable Integer productoId, @PathVariable Integer proveedorId) {
        productoProveedorService.delete(productoId, proveedorId);
    }
}
