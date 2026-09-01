package co.salonglitt.service;

import co.salonglitt.dto.NotificacionRequestDTO;
import co.salonglitt.dto.NotificacionResponseDTO;
import co.salonglitt.entity.Notificacion;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.NotificacionRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<NotificacionResponseDTO> findAll() {
        return notificacionRepository.findAll().stream().map(this::aDto).toList();
    }

    public NotificacionResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<NotificacionResponseDTO> findByUsuario(Long usuarioId) {
        validarUsuario(usuarioId);
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(this::aDto).toList();
    }

    public NotificacionResponseDTO create(NotificacionRequestDTO dto) {
        var usuario = validarUsuario(dto.usuarioId());
        Notificacion n = new Notificacion(usuario, dto.titulo().trim(), dto.mensaje().trim(), false, null);
        return aDto(notificacionRepository.save(n));
    }

    public NotificacionResponseDTO marcarLeida(Long id) {
        Notificacion actual = obtener(id);
        actual.setLeida(true);
        return aDto(notificacionRepository.save(actual));
    }

    public void marcarTodasLeidas(Long usuarioId) {
        validarUsuario(usuarioId);
        notificacionRepository.findByUsuarioIdAndLeidaFalse(usuarioId).forEach(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
        });
    }

    public void delete(Long id) {
        obtener(id);
        notificacionRepository.deleteById(id);
    }

    private Notificacion obtener(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada con id " + id));
    }

    private Usuario validarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private NotificacionResponseDTO aDto(Notificacion n) {
        return new NotificacionResponseDTO(n.getId(), n.getUsuario().getId(), n.getTitulo(), n.getMensaje(),
                n.isLeida(), n.getFechaCreacion());
    }
}
