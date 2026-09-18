package co.salonglitt.service;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.entity.Agenda;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.AgendaRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AgendaService service;

    private Usuario usuario(Integer id, String nombre) {
        Usuario u = new Usuario(nombre, nombre + "@mail.com", "123", "300000", "estilista");
        u.setId(id);
        return u;
    }

    private Agenda agenda(Integer id, Usuario u) {
        Agenda a = new Agenda("lunes", "09:00", "14:00", u);
        a.setId(id);
        return a;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario(1, "laura")));
        when(agendaRepository.save(any(Agenda.class))).thenAnswer(inv -> {
            Agenda a = inv.getArgument(0);
            a.setId(1);
            return a;
        });

        AgendaResponseDTO created = service.create(new AgendaRequestDTO("lunes", "09:00", "14:00", 1));

        assertNotNull(created.id());
        assertEquals("lunes", created.diasemana());
        assertEquals("09:00", created.horainicio());
        assertEquals("14:00", created.horafin());
        assertEquals(1, created.usuarioId());
    }

    @Test
    void create_shouldThrowWhenHoraInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create(new AgendaRequestDTO("lunes", "14:00", "09:00", 1)));
    }

    @Test
    void findAll_shouldReturnAllBlocks() {
        when(agendaRepository.findAll()).thenReturn(List.of(agenda(1, usuario(1, "laura"))));

        List<AgendaResponseDTO> agendas = service.findAll();

        assertEquals(1, agendas.size());
        assertEquals("lunes", agendas.get(0).diasemana());
    }

    @Test
    void findByUsuario_shouldReturnBlocks() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario(1, "laura")));
        when(agendaRepository.findByUsuarioId(1)).thenReturn(List.of(agenda(1, usuario(1, "laura"))));

        List<AgendaResponseDTO> porUsuario = service.findByUsuario(1);

        assertEquals(1, porUsuario.size());
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(agendaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(999));
    }

    @Test
    void findById_shouldThrowWhenBlockDoesNotExist() {
        when(agendaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }
}