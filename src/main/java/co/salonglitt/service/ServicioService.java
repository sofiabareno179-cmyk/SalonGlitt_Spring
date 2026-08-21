package co.salonglitt.service;

import co.salonglitt.dto.ServicioRequestDTO;
import co.salonglitt.dto.ServicioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ServicioService {

    private record Servicio(Long id, String nombre, String descripcion,
                            java.math.BigDecimal precio, Integer duracionMinutos, boolean activo) {
    }

    private final Map<Long, Servicio> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    public List<ServicioResponseDTO> findAll() {
        return datos.values().stream().map(this::aDto).toList();
    }

    public ServicioResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public ServicioResponseDTO create(ServicioRequestDTO dto) {
        long id = secuencia.incrementAndGet();
        Servicio s = new Servicio(id, dto.nombre().trim(), dto.descripcion(), dto.precio(),
                dto.duracionMinutos(), dto.activo() == null || dto.activo());
        datos.put(id, s);
        return aDto(s);
    }

    public ServicioResponseDTO update(Long id, ServicioRequestDTO dto) {
        Servicio actual = obtener(id);
        Servicio s = new Servicio(actual.id(), dto.nombre().trim(), dto.descripcion(), dto.precio(),
                dto.duracionMinutos(), dto.activo() == null || dto.activo());
        datos.put(id, s);
        return aDto(s);
    }

    public void delete(Long id) {
        obtener(id);
        datos.remove(id);
    }

    private Servicio obtener(Long id) {
        Servicio s = datos.get(id);
        if (s == null) {
            throw new NotFoundException("Servicio no encontrado con id " + id);
        }
        return s;
    }

    private ServicioResponseDTO aDto(Servicio s) {
        return new ServicioResponseDTO(s.id(), s.nombre(), s.descripcion(), s.precio(),
                s.duracionMinutos(), s.activo());
    }
}