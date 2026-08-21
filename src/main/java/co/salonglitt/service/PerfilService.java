package co.salonglitt.service;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PerfilService {

    private record Perfil(Long id, String nombre, String descripcion) {
    }

    private final Map<Long, Perfil> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    public List<PerfilResponseDTO> findAll() {
        return datos.values().stream()
                .map(p -> new PerfilResponseDTO(p.id(), p.nombre(), p.descripcion()))
                .toList();
    }

    public PerfilResponseDTO findById(Long id) {
        Perfil p = obtener(id);
        return new PerfilResponseDTO(p.id(), p.nombre(), p.descripcion());
    }

    public PerfilResponseDTO create(PerfilRequestDTO dto) {
        long id = secuencia.incrementAndGet();
        Perfil p = new Perfil(id, dto.nombre().trim(), dto.descripcion());
        datos.put(id, p);
        return new PerfilResponseDTO(p.id(), p.nombre(), p.descripcion());
    }

    public PerfilResponseDTO update(Long id, PerfilRequestDTO dto) {
        Perfil actual = obtener(id);
        Perfil p = new Perfil(actual.id(), dto.nombre().trim(), dto.descripcion());
        datos.put(id, p);
        return new PerfilResponseDTO(p.id(), p.nombre(), p.descripcion());
    }

    public void delete(Long id) {
        obtener(id);
        datos.remove(id);
    }

    private Perfil obtener(Long id) {
        Perfil p = datos.get(id);
        if (p == null) {
            throw new NotFoundException("Perfil no encontrado con id " + id);
        }
        return p;
    }
}