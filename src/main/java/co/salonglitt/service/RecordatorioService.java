package co.salonglitt.service;

import co.salonglitt.dto.RecordatorioRequestDTO;
import co.salonglitt.dto.RecordatorioResponseDTO;
import co.salonglitt.entity.Recordatorio;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.RecordatorioRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordatorioService {

    private final RecordatorioRepository recordatorioRepository;
    private final UsuarioRepository usuarioRepository;

    public RecordatorioService(RecordatorioRepository recordatorioRepository, UsuarioRepository usuarioRepository) {
        this.recordatorioRepository = recordatorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<RecordatorioResponseDTO> findAll() {
        return recordatorioRepository.findAll().stream().map(this::aDto).toList();
    }

    public RecordatorioResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<RecordatorioResponseDTO> findByUsuario(Integer usuarioId) {
        validarUsuario(usuarioId);
        return recordatorioRepository.findByUsuarioId(usuarioId).stream().map(this::aDto).toList();
    }

    public RecordatorioResponseDTO create(RecordatorioRequestDTO dto) {
        Usuario usuario = validarUsuario(dto.idusuario());
        Recordatorio r = new Recordatorio(dto.titulo().trim(), dto.mensaje(),
                dto.fecha_recordatorio().trim(), usuario);
        return aDto(recordatorioRepository.save(r));
    }

    public RecordatorioResponseDTO update(Integer id, RecordatorioRequestDTO dto) {
        Recordatorio actual = obtener(id);
        Usuario usuario = validarUsuario(dto.idusuario());
        actual.setTitulo(dto.titulo().trim());
        actual.setMensaje(dto.mensaje());
        actual.setFechaRecordatorio(dto.fecha_recordatorio().trim());
        actual.setUsuario(usuario);
        return aDto(recordatorioRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        recordatorioRepository.deleteById(id);
    }

    private Recordatorio obtener(Integer id) {
        return recordatorioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recordatorio no encontrado con id " + id));
    }

    private Usuario validarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private RecordatorioResponseDTO aDto(Recordatorio r) {
        return new RecordatorioResponseDTO(
                r.getId(),
                r.getTitulo(),
                r.getMensaje(),
                r.getFechaRecordatorio(),
                r.getUsuario() != null ? r.getUsuario().getId() : null);
    }
}