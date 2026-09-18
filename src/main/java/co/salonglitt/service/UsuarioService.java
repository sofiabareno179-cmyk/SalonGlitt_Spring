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

    public UsuarioResponseDTO findByEmail(String email) {
        Usuario u = usuarioRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email " + email));
        return aDto(u);
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        validarEmailUnico(dto.email().trim(), null);
        validarNombreUserUnico(dto.nombreuser().trim(), null);
        Usuario u = new Usuario(dto.nombreuser().trim(), dto.email().trim().toLowerCase(),
                dto.passwordHash(), dto.telefono(), dto.rol().trim());
        return aDto(usuarioRepository.save(u));
    }

    public UsuarioResponseDTO update(Integer id, UsuarioRequestDTO dto) {
        Usuario actual = obtener(id);
        validarEmailUnico(dto.email().trim(), id);
        validarNombreUserUnico(dto.nombreuser().trim(), id);
        actual.setNombreuser(dto.nombreuser().trim());
        actual.setEmail(dto.email().trim().toLowerCase());
        actual.setPasswordHash(dto.passwordHash());
        actual.setTelefono(dto.telefono());
        actual.setRol(dto.rol().trim());
        return aDto(usuarioRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario obtener(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private void validarEmailUnico(String email, Integer exceptoId) {
        usuarioRepository.findByEmailIgnoreCase(email)
                .filter(u -> exceptoId == null || !u.getId().equals(exceptoId))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe un usuario con el email " + email);
                });
    }

    private void validarNombreUserUnico(String nombreuser, Integer exceptoId) {
        List<Usuario> existentes = usuarioRepository.findAll();
        boolean existe = existentes.stream()
                .anyMatch(u -> u.getNombreuser().equalsIgnoreCase(nombreuser)
                        && (exceptoId == null || !u.getId().equals(exceptoId)));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un usuario con el nombre de usuario " + nombreuser);
        }
    }

    private UsuarioResponseDTO aDto(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNombreuser(), u.getEmail(), u.getTelefono(), u.getRol());
    }
}