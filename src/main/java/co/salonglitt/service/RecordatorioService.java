package co.salonglitt.service;

import co.salonglitt.dto.RecordatorioRequestDTO;
import co.salonglitt.dto.RecordatorioResponseDTO;
import co.salonglitt.entity.Cita;
import co.salonglitt.entity.Recordatorio;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CitaRepository;
import co.salonglitt.repository.RecordatorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordatorioService {

    private final RecordatorioRepository recordatorioRepository;
    private final CitaRepository citaRepository;

    public RecordatorioService(RecordatorioRepository recordatorioRepository, CitaRepository citaRepository) {
        this.recordatorioRepository = recordatorioRepository;
        this.citaRepository = citaRepository;
    }

    public List<RecordatorioResponseDTO> findAll() {
        return recordatorioRepository.findAll().stream().map(this::aDto).toList();
    }

    public RecordatorioResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<RecordatorioResponseDTO> findByCita(Long citaId) {
        validarCita(citaId);
        return recordatorioRepository.findByCitaId(citaId).stream().map(this::aDto).toList();
    }

    public RecordatorioResponseDTO create(RecordatorioRequestDTO dto) {
        var cita = validarCita(dto.citaId());
        Recordatorio r = new Recordatorio(cita, dto.fechaEnvio(), dto.tipo().trim().toUpperCase(), false);
        return aDto(recordatorioRepository.save(r));
    }

    public RecordatorioResponseDTO marcarEnviado(Long id) {
        Recordatorio actual = obtener(id);
        actual.setEnviado(true);
        return aDto(recordatorioRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        recordatorioRepository.deleteById(id);
    }

    private Recordatorio obtener(Long id) {
        return recordatorioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recordatorio no encontrado con id " + id));
    }

    private Cita validarCita(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con id " + id));
    }

    private RecordatorioResponseDTO aDto(Recordatorio r) {
        return new RecordatorioResponseDTO(r.getId(), r.getCita().getId(), r.getFechaEnvio(),
                r.getTipo(), r.isEnviado());
    }
}
