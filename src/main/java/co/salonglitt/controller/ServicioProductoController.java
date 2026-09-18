package co.salonglitt.controller;

import co.salonglitt.dto.ServicioProductoRequestDTO;
import co.salonglitt.dto.ServicioProductoResponseDTO;
import co.salonglitt.service.ServicioProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicio-productos")
@Tag(name = "Servicio-Producto", description = "Relación N:N entre servicios y productos")
public class ServicioProductoController {

    private final ServicioProductoService servicioProductoService;

    public ServicioProductoController(ServicioProductoService servicioProductoService) {
        this.servicioProductoService = servicioProductoService;
    }

    @GetMapping
    @Operation(summary = "Listar relaciones", description = "Filtra opcionalmente por servicio o producto")
    public List<ServicioProductoResponseDTO> listar(@RequestParam(required = false) Integer servicioId,
                                                    @RequestParam(required = false) Integer productoId) {
        if (servicioId != null) return servicioProductoService.findByServicio(servicioId);
        if (productoId != null) return servicioProductoService.findByProducto(productoId);
        return servicioProductoService.findAll();
    }

    @GetMapping("/{servicioId}/{productoId}")
    @Operation(summary = "Obtener relación por servicio y producto")
    public ServicioProductoResponseDTO obtener(@PathVariable Integer servicioId, @PathVariable Integer productoId) {
        return servicioProductoService.findById(servicioId, productoId);
    }

    @PostMapping
    @Operation(summary = "Crear relación servicio-producto")
    public ResponseEntity<ServicioProductoResponseDTO> crear(@Valid @RequestBody ServicioProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioProductoService.create(dto));
    }

    @DeleteMapping("/{servicioId}/{productoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar relación servicio-producto")
    public void eliminar(@PathVariable Integer servicioId, @PathVariable Integer productoId) {
        servicioProductoService.delete(servicioId, productoId);
    }
}
