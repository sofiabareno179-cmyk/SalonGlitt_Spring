package co.salonglitt.service;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.entity.Perfil;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.PerfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioService usuarioService;

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

        Usuario usuario = usuarioService.obtener(dto.usuarioId());
        Perfil p = new Perfil(dto.nombre().trim(), dto.apellido(), dto.bio(), usuario);
        return aDto(perfilRepository.save(p));

        validarNombreUnico(dto.nombre().trim(), null);
        long id = secuencia.incrementAndGet();
        Perfil p = new Perfil(id, dto.nombre().trim(), dto.descripcion());
        datos.put(id, p);
        return new PerfilResponseDTO(p.id(), p.nombre(), p.descripcion());

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
        Perfil p = new Perfil(actual.id(), dto.nombre().trim(), dto.descripcion());
        datos.put(id, p);
        return new PerfilResponseDTO(p.id(), p.nombre(), p.descripcion());

    }

    public void delete(Integer id) {
        Perfil p = obtener(id);
        perfilRepository.delete(p);
    }

    public Perfil obtener(Integer id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con id " + id));
    }

    private PerfilResponseDTO aDto(Perfil p) {
        return new PerfilResponseDTO(p.getId(), p.getNombre(), p.getApellido(), p.getBio(),
                p.getUsuario().getId(), p.getUsuario().getNombreuser());
    }
}
    private void validarNombreUnico(String nombre, Long exceptoId) {
        datos.values().stream()
                .filter(p -> p.nombre().equalsIgnoreCase(nombre))
                .filter(p -> exceptoId == null || !p.id().equals(exceptoId))
                .findFirst()
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Ya existe un perfil con el nombre " + nombre);
                });
    }

