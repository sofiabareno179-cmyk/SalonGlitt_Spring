package co.salonglitt.service;

import co.salonglitt.dto.UsuarioRequestDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this(usuarioRepository, null, new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder());
    }

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this(usuarioRepository, perfilRepository, new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder());
    }

    @org.springframework.beans.factory.annotation.Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder == null
            ? new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
            : passwordEncoder;
    }

    public List<UsuarioResponseDTO> findAll() { return usuarioRepository.findAll().stream().map(this::aDto).toList(); }
    public UsuarioResponseDTO findById(Integer id) { return aDto(obtener(id)); }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        validarEmailUnico(dto.email().trim(), null);
        Perfil perfil = perfilRepository == null ? new Perfil(dto.rol().trim(), dto.rol()) : obtenerPerfilPorNombre(dto.rol().trim());
        Usuario usuario = new Usuario(dto.nombreuser().trim(), dto.email().trim(), dto.telefono(), perfil, true);
        usuario.setPasswordHash(passwordEncoder.encode(dto.passwordHash()));
        usuario.setRol(dto.rol().trim());
        return aDto(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO update(Integer id, UsuarioRequestDTO dto) {
        Usuario actual = obtener(id);
        validarEmailUnico(dto.email().trim(), id.longValue());
        actual.setNombre(dto.nombreuser().trim());
        actual.setEmail(dto.email().trim());
        actual.setTelefono(dto.telefono());
        actual.setPerfil(perfilRepository == null ? new Perfil(dto.rol().trim(), dto.rol()) : obtenerPerfilPorNombre(dto.rol().trim()));
        actual.setPasswordHash(passwordEncoder.encode(dto.passwordHash()));
        actual.setRol(dto.rol().trim());
        return aDto(usuarioRepository.save(actual));
    }

    public void delete(Integer id) { usuarioRepository.delete(obtener(id)); }

    public Usuario obtener(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private Perfil obtenerPerfilPorNombre(String nombre) {
        return perfilRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con nombre " + nombre));
    }

    private void validarEmailUnico(String email, Long exceptoId) {
        usuarioRepository.findByEmailIgnoreCase(email)
                .filter(usuario -> exceptoId == null || !usuario.getId().equals(exceptoId))
                .ifPresent(usuario -> { throw new IllegalArgumentException("Ya existe un usuario con el email " + email); });
    }

    private UsuarioResponseDTO aDto(Usuario usuario) {
        String rol = usuario.getPerfil() == null ? usuario.getRol() : usuario.getPerfil().getNombre();
        return new UsuarioResponseDTO(Math.toIntExact(usuario.getId()), usuario.getNombre(), usuario.getEmail(),
            usuario.getTelefono(), rol);
    }
}