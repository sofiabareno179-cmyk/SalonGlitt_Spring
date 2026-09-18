package co.salonglitt.service;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.entity.Agenda;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.AgendaRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendaService(AgendaRepository agendaRepository, UsuarioRepository usuarioRepository) {
        this.agendaRepository = agendaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<AgendaResponseDTO> findAll() {
        return agendaRepository.findAll().stream().map(this::aDto).toList();
    }

    public AgendaResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<AgendaResponseDTO> findByUsuario(Integer usuarioId) {
        validarUsuario(usuarioId);
        return agendaRepository.findByUsuarioId(usuarioId).stream().map(this::aDto).toList();
    }

    public AgendaResponseDTO create(AgendaRequestDTO dto) {
        validarHoras(dto.horainicio(), dto.horafin());
        Usuario usuario = validarUsuario(dto.usuarioId());
        Agenda a = new Agenda(dto.diasemana().trim(), dto.horainicio().trim(), dto.horafin().trim(), usuario);
        return aDto(agendaRepository.save(a));
    }

    public AgendaResponseDTO update(Integer id, AgendaRequestDTO dto) {
        Agenda actual = obtener(id);
        validarHoras(dto.horainicio(), dto.horafin());
        Usuario usuario = validarUsuario(dto.usuarioId());
        actual.setDiasemana(dto.diasemana().trim());
        actual.setHorainicio(dto.horainicio().trim());
        actual.setHorafin(dto.horafin().trim());
        actual.setUsuario(usuario);
        return aDto(agendaRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        agendaRepository.deleteById(id);
    }

    private Agenda obtener(Integer id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bloque de agenda no encontrado con id " + id));
    }

    private Usuario validarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private void validarHoras(String inicio, String fin) {
        if (inicio != null && fin != null && inicio.trim().compareTo(fin.trim()) >= 0) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
    }

    private AgendaResponseDTO aDto(Agenda a) {
        return new AgendaResponseDTO(a.getId(), a.getDiasemana(), a.getHorainicio(), a.getHorafin(),
                a.getUsuario().getId(), a.getUsuario().getNombreuser());
    }
}