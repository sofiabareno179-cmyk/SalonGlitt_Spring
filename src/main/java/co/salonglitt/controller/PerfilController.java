package co.salonglitt.controller;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
@Tag(name = "Perfiles", description = "Datos de perfil de cada usuario (1:1)")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    @Operation(summary = "Listar perfiles", description = "Retorna todos los perfiles")
    public List<PerfilResponseDTO> listar() { return perfilService.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener perfil por ID")
    public PerfilResponseDTO obtener(@PathVariable Integer id) { return perfilService.findById(id); }

    @PostMapping
    @Operation(summary = "Crear perfil", description = "El usuario debe existir previamente")
    public ResponseEntity<PerfilResponseDTO> crear(@Valid @RequestBody PerfilRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar perfil")
    public PerfilResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody PerfilRequestDTO dto) {
        return perfilService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar perfil")
    public void eliminar(@PathVariable Integer id) { perfilService.delete(id); }
}