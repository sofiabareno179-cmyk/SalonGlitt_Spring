package co.salonglitt.service;

import co.salonglitt.dto.UsuarioRequestDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream().map(this::aDto).toList();
    }

    public UsuarioResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
<<<<<<< HEAD
        Usuario u = new Usuario(dto.nombreuser().trim(), dto.email().trim(),
                dto.passwordHash(), dto.telefono(), dto.rol().trim());
        return aDto(usuarioRepository.save(u));
    }

    public UsuarioResponseDTO update(Integer id, UsuarioRequestDTO dto) {
        Usuario actual = obtener(id);
        actual.setNombreuser(dto.nombreuser().trim());
        actual.setEmail(dto.email().trim());
        actual.setPasswordHash(dto.passwordHash());
        actual.setTelefono(dto.telefono());
        actual.setRol(dto.rol().trim());
        return aDto(usuarioRepository.save(actual));
=======
        var perfil = perfilService.findById(dto.perfilId());
        validarEmailUnico(dto.email().trim(), null);
        long id = secuencia.incrementAndGet();
        Usuario u = new Usuario(id, dto.nombre().trim(), dto.email().trim(), dto.telefono(),
                perfil.id(), perfil.nombre(), dto.activo() == null || dto.activo());
        datos.put(id, u);
        return aDto(u);
    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        obtener(id);
        var perfil = perfilService.findById(dto.perfilId());
        validarEmailUnico(dto.email().trim(), id);
        Usuario u = new Usuario(id, dto.nombre().trim(), dto.email().trim(), dto.telefono(),
                perfil.id(), perfil.nombre(), dto.activo() == null || dto.activo());
        datos.put(id, u);
        return aDto(u);
>>>>>>> c941d9769e25da0beb4b0ae1a46f61ac155acdec
    }

    public void delete(Integer id) {
        Usuario u = obtener(id);
        usuarioRepository.delete(u);
    }

    public Usuario obtener(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private void validarEmailUnico(String email, Long exceptoId) {
        datos.values().stream()
                .filter(u -> u.email().equalsIgnoreCase(email))
                .filter(u -> exceptoId == null || !u.id().equals(exceptoId))
                .findFirst()
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe un usuario con el email " + email);
                });
    }

    private void validarEmailUnico(String email, Long exceptoId) {
        datos.values().stream()
                .filter(u -> u.email().equalsIgnoreCase(email))
                .filter(u -> exceptoId == null || !u.id().equals(exceptoId))
                .findFirst()
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe un usuario con el email " + email);
                });
    }

    private UsuarioResponseDTO aDto(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNombreuser(), u.getEmail(), u.getTelefono(), u.getRol());
    }
}
