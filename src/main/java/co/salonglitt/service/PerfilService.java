package co.salonglitt.service;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.entity.Perfil;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.PerfilRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilService(PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<PerfilResponseDTO> findAll() {
        return perfilRepository.findAll().stream().map(this::aDto).toList();
    }

    public PerfilResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public PerfilResponseDTO findByUsuario(Integer usuarioId) {
        Perfil p = perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado para el usuario " + usuarioId));
        return aDto(p);
    }

    public PerfilResponseDTO create(PerfilRequestDTO dto) {
        validarPerfilUnico(dto.idusuario(), null);
        Usuario usuario = validarUsuario(dto.idusuario());
        Perfil p = new Perfil(dto.nombre().trim(), dto.apellido(), dto.bio(), usuario);
        return aDto(perfilRepository.save(p));
    }

    public PerfilResponseDTO update(Integer id, PerfilRequestDTO dto) {
        Perfil actual = obtener(id);
        validarPerfilUnico(dto.idusuario(), id);
        Usuario usuario = validarUsuario(dto.idusuario());
        actual.setNombre(dto.nombre().trim());
        actual.setApellido(dto.apellido());
        actual.setBio(dto.bio());
        actual.setUsuario(usuario);
        return aDto(perfilRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        perfilRepository.deleteById(id);
    }

    private Perfil obtener(Integer id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con id " + id));
    }

    private Usuario validarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private void validarPerfilUnico(Integer usuarioId, Integer exceptoId) {
        perfilRepository.findByUsuarioId(usuarioId)
                .filter(p -> exceptoId == null || !p.getId().equals(exceptoId))
                .ifPresent(p -> {
                    throw new IllegalArgumentException("El usuario " + usuarioId + " ya tiene un perfil");
                });
    }

    private PerfilResponseDTO aDto(Perfil p) {
        return new PerfilResponseDTO(p.getId(), p.getNombre(), p.getApellido(), p.getBio(),
                p.getUsuario().getId(), p.getUsuario().getNombreuser());
    }
}