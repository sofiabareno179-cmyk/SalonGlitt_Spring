package co.salonglitt.service;

import co.salonglitt.dto.UsuarioRequestDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.entity.Perfil;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.PerfilRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream().map(this::aDto).toList();
    }

    public UsuarioResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        var perfil = obtenerPerfil(dto.perfilId());
        validarEmailUnico(dto.email().trim(), null);
        Usuario u = new Usuario(dto.nombre().trim(), dto.email().trim(), dto.telefono(),
                perfil, dto.activo() == null || dto.activo());
        return aDto(usuarioRepository.save(u));
    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario actual = obtener(id);
        var perfil = obtenerPerfil(dto.perfilId());
        validarEmailUnico(dto.email().trim(), id);
        actual.setNombre(dto.nombre().trim());
        actual.setEmail(dto.email().trim());
        actual.setTelefono(dto.telefono());
        actual.setPerfil(perfil);
        actual.setActivo(dto.activo() == null || dto.activo());
        return aDto(usuarioRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private Perfil obtenerPerfil(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con id " + id));
    }

    private void validarEmailUnico(String email, Long exceptoId) {
        usuarioRepository.findByEmailIgnoreCase(email)
                .filter(u -> exceptoId == null || !u.getId().equals(exceptoId))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe un usuario con el email " + email);
                });
    }

    private UsuarioResponseDTO aDto(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNombre(), u.getEmail(), u.getTelefono(),
                u.getPerfil().getId(), u.getPerfil().getNombre(), u.isActivo());
    }
}
