package co.salonglitt.service;

import co.salonglitt.dto.PromocionRequestDTO;
import co.salonglitt.dto.PromocionResponseDTO;
import co.salonglitt.entity.Promocion;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromocionService {

    private final PromocionRepository promocionRepository;

    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    public List<PromocionResponseDTO> findAll() {
        return promocionRepository.findAll().stream().map(this::aDto).toList();
    }

    public PromocionResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<PromocionResponseDTO> findActivas() {
        return promocionRepository.findByActivaTrue().stream().map(this::aDto).toList();
    }

    public PromocionResponseDTO create(PromocionRequestDTO dto) {
        Promocion p = new Promocion(dto.titulo().trim(), dto.descripcion(),
                dto.activa() == null ? Boolean.TRUE : dto.activa());
        return aDto(promocionRepository.save(p));
    }

    public PromocionResponseDTO update(Integer id, PromocionRequestDTO dto) {
        Promocion actual = obtener(id);
        actual.setTitulo(dto.titulo().trim());
        actual.setDescripcion(dto.descripcion());
        actual.setActiva(dto.activa());
        actual.setUpdatedAt(LocalDateTime.now());
        return aDto(promocionRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        promocionRepository.deleteById(id);
    }

    private Promocion obtener(Integer id) {
        return promocionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con id " + id));
    }

    private PromocionResponseDTO aDto(Promocion p) {
        return new PromocionResponseDTO(p.getId(), p.getTitulo(), p.getDescripcion(),
                p.getActiva(), p.getUpdatedAt());
    }
}