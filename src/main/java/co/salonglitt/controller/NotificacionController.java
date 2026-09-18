package co.salonglitt.controller;

import co.salonglitt.dto.NotificacionRequestDTO;
import co.salonglitt.dto.NotificacionResponseDTO;
import co.salonglitt.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "Notificaciones dirigidas a los usuarios")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @Operation(summary = "Listar notificaciones")
    public List<NotificacionResponseDTO> listar() {
        return notificacionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificación por ID")
    public NotificacionResponseDTO obtener(@PathVariable Integer id) {
        return notificacionService.findById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar notificaciones de un usuario")
    public List<NotificacionResponseDTO> listarPorUsuario(@PathVariable Integer usuarioId) {
        return notificacionService.findByUsuario(usuarioId);
    }

    @PostMapping
    @Operation(summary = "Crear notificación", description = "El usuario debe existir")
    public ResponseEntity<NotificacionResponseDTO> crear(@Valid @RequestBody NotificacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionService.create(dto));
    }

    @PatchMapping("/{id}/leida")
    @Operation(summary = "Marcar notificación como leída")
    public NotificacionResponseDTO marcarLeida(@PathVariable Integer id) {
        return notificacionService.marcarLeida(id);
    }

    @PatchMapping("/usuario/{usuarioId}/leidas")
    @Operation(summary = "Marcar todas las notificaciones de un usuario como leídas")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void marcarTodasLeidas(@PathVariable Integer usuarioId) {
        notificacionService.marcarTodasLeidas(usuarioId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar notificación")
    public void eliminar(@PathVariable Integer id) {
        notificacionService.delete(id);
    }
}