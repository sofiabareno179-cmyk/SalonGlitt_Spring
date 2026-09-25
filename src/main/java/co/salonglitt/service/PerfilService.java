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
    private final UsuarioService usuarioService;

    public PerfilService(PerfilRepository perfilRepository) {
        this(perfilRepository, null);
    }

    @org.springframework.beans.factory.annotation.Autowired
    public PerfilService(PerfilRepository perfilRepository, UsuarioService usuarioService) {
        this.perfilRepository = perfilRepository;
        this.usuarioService = usuarioService;
    }

    public List<PerfilResponseDTO> findAll() {
        return perfilRepository.findAll().stream().map(this::aDto).toList();
    }

    public PerfilResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public PerfilResponseDTO create(PerfilRequestDTO dto) {
        validarNombreUnico(dto.nombre().trim(), null);
        Usuario usuario = usuarioService == null ? null : usuarioService.obtener(dto.usuarioId());
        Perfil p = new Perfil(dto.nombre().trim(), dto.apellido(), dto.bio(), usuario);
        return aDto(perfilRepository.save(p));
    }

    public PerfilResponseDTO update(Integer id, PerfilRequestDTO dto) {
        Perfil actual = obtener(id);
        Usuario usuario = usuarioService == null ? null : usuarioService.obtener(dto.usuarioId());
        validarNombreUnico(dto.nombre().trim(), id.longValue());
        actual.setNombre(dto.nombre().trim());
        actual.setApellido(dto.apellido());
        actual.setBio(dto.bio());
        actual.setUsuario(usuario);
        actual.setDescripcion(dto.bio());
        return aDto(perfilRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        perfilRepository.delete(obtener(id));
    }

    private Perfil obtener(Integer id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con id " + id));
    }

    private PerfilResponseDTO aDto(Perfil p) {
        Integer usuarioId = p.getUsuario() == null || p.getUsuario().getId() == null ? null : Math.toIntExact(p.getUsuario().getId());
        String usuarioNombre = p.getUsuario() == null ? null : p.getUsuario().getNombre();
        return new PerfilResponseDTO(Math.toIntExact(p.getId()), p.getNombre(), p.getApellido(), p.getBio(), usuarioId, usuarioNombre);
    }

    private void validarNombreUnico(String nombre, Long exceptoId) {
        perfilRepository.findByNombreIgnoreCase(nombre)
                .filter(p -> exceptoId == null || !p.getId().equals(exceptoId))
                .ifPresent(p -> {
                    throw new IllegalArgumentException("El usuario " + usuarioId + " ya tiene un perfil");
                });
    }

}
