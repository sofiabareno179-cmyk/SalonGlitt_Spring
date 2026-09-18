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

    public NotificacionResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<NotificacionResponseDTO> findByUsuario(Integer usuarioId) {
        validarUsuario(usuarioId);
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(this::aDto).toList();
    }

    public NotificacionResponseDTO create(NotificacionRequestDTO dto) {
        Usuario usuario = validarUsuario(dto.idusuario());
        Notificacion n = new Notificacion(usuario, dto.titulo().trim(), dto.mensaje(), false, null);
        return aDto(notificacionRepository.save(n));
    }

    public NotificacionResponseDTO marcarLeida(Integer id) {
        Notificacion actual = obtener(id);
        actual.setLeida(true);
        return aDto(notificacionRepository.save(actual));
    }

    public void marcarTodasLeidas(Integer usuarioId) {
        validarUsuario(usuarioId);
        notificacionRepository.findByUsuarioIdAndLeidaFalse(usuarioId).forEach(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
        });
    }

    public void delete(Integer id) {
        obtener(id);
        notificacionRepository.deleteById(id);
    }

    private Notificacion obtener(Integer id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada con id " + id));
    }

    private Usuario validarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private NotificacionResponseDTO aDto(Notificacion n) {
        return new NotificacionResponseDTO(n.getId(), n.getUsuario().getId(),
                n.getTitulo(), n.getMensaje(), n.getLeida(), n.getFechaCreacion());
    }
}