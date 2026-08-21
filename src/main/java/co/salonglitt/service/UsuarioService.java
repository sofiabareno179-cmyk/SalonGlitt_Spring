package co.salonglitt.service;

import co.salonglitt.dto.UsuarioRequestDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UsuarioService {

    private record Usuario(Long id, String nombre, String email, String telefono,
                           Long perfilId, String perfilNombre, boolean activo) {
    }

    private final Map<Long, Usuario> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();
    private final PerfilService perfilService;

    public UsuarioService(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    public List<UsuarioResponseDTO> findAll() {
        return datos.values().stream()
                .map(u -> new UsuarioResponseDTO(u.id(), u.nombre(), u.email(), u.telefono(),
                        u.perfilId(), u.perfilNombre(), u.activo()))
                .toList();
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario u = obtener(id);
        return aDto(u);
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        var perfil = perfilService.findById(dto.perfilId());
        long id = secuencia.incrementAndGet();
        Usuario u = new Usuario(id, dto.nombre().trim(), dto.email().trim(), dto.telefono(),
                perfil.id(), perfil.nombre(), dto.activo() == null || dto.activo());
        datos.put(id, u);
        return aDto(u);
    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario actual = obtener(id);
        var perfil = perfilService.findById(dto.perfilId());
        Usuario u = new Usuario(actual.id(), dto.nombre().trim(), dto.email().trim(), dto.telefono(),
                perfil.id(), perfil.nombre(), dto.activo() == null || dto.activo());
        datos.put(id, u);
        return aDto(u);
    }

    public void delete(Long id) {
        obtener(id);
        datos.remove(id);
    }

    private Usuario obtener(Long id) {
        Usuario u = datos.get(id);
        if (u == null) {
            throw new NotFoundException("Usuario no encontrado con id " + id);
        }
        return u;
    }

    private UsuarioResponseDTO aDto(Usuario u) {
        return new UsuarioResponseDTO(u.id(), u.nombre(), u.email(), u.telefono(),
                u.perfilId(), u.perfilNombre(), u.activo());
    }
}