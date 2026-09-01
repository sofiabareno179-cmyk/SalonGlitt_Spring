package co.salonglitt.service;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.entity.Perfil;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.PerfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public List<PerfilResponseDTO> findAll() {
        return perfilRepository.findAll().stream().map(this::aDto).toList();
    }

    public PerfilResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public PerfilResponseDTO create(PerfilRequestDTO dto) {

        Usuario usuario = usuarioService.obtener(dto.usuarioId());
        Perfil p = new Perfil(dto.nombre().trim(), dto.apellido(), dto.bio(), usuario);
        return aDto(perfilRepository.save(p));

        validarNombreUnico(dto.nombre().trim(), null);
        Perfil p = new Perfil(dto.nombre().trim(), dto.descripcion());
        return aDto(perfilRepository.save(p));
    }

    public PerfilResponseDTO update(Integer id, PerfilRequestDTO dto) {
        Perfil actual = obtener(id);
        Usuario usuario = usuarioService.obtener(dto.usuarioId());
        actual.setNombre(dto.nombre().trim());
        actual.setApellido(dto.apellido());
        actual.setBio(dto.bio());
        actual.setUsuario(usuario);
        return aDto(perfilRepository.save(actual));

        validarNombreUnico(dto.nombre().trim(), id);
        actual.setNombre(dto.nombre().trim());
        actual.setDescripcion(dto.descripcion());
        return aDto(perfilRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        perfilRepository.deleteById(id);
    }

    private Perfil obtener(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con id " + id));
    }

    private PerfilResponseDTO aDto(Perfil p) {
        return new PerfilResponseDTO(p.getId(), p.getNombre(), p.getApellido(), p.getBio(),
                p.getUsuario().getId(), p.getUsuario().getNombreuser());
    }
}
    private void validarNombreUnico(String nombre, Long exceptoId) {
        perfilRepository.findByNombreIgnoreCase(nombre)
                .filter(p -> exceptoId == null || !p.getId().equals(exceptoId))
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Ya existe un perfil con el nombre " + nombre);
                });
    }

    private PerfilResponseDTO aDto(Perfil p) {
        return new PerfilResponseDTO(p.getId(), p.getNombre(), p.getDescripcion());
    }
}
