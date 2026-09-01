package co.salonglitt.service;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.entity.Agenda;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.AgendaRepository;
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
    private UsuarioService usuarioService;

    @InjectMocks
    private AgendaService service;

    private Usuario usuario(Integer id, String nombreuser) {
        Usuario u = new Usuario(nombreuser, nombreuser + "@mail.com", "hash", "123", "estilista");
        u.setId(id);
        return u;
    }

    private Agenda bloque(Integer id, Usuario usuario) {
        Agenda a = new Agenda("lunes", "09:00", "14:00", usuario);
        a.setId(id);
        return a;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        Usuario usuario = usuario(1, "laura");
        when(usuarioService.obtener(1)).thenReturn(usuario);
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
        assertEquals("laura", created.usuarioNombre());
    }

    @Test
    void findAll_shouldReturnAllBlocks() {
        Usuario u1 = usuario(1, "laura");
        Usuario u2 = usuario(2, "juan");
        when(agendaRepository.findAll()).thenReturn(List.of(
                bloque(1, u1),
                bloque(2, u2)));

        List<AgendaResponseDTO> agendas = service.findAll();

        assertEquals(2, agendas.size());
        assertTrue(agendas.stream().anyMatch(a -> a.diasemana().equals("lunes")));
    }

    @Test
    void findById_shouldReturnSavedBlock() {
        Usuario usuario = usuario(1, "laura");
        when(agendaRepository.findById(1)).thenReturn(Optional.of(bloque(1, usuario)));

        AgendaResponseDTO found = service.findById(1);

        assertEquals(1, found.id());
        assertEquals("lunes", found.diasemana());
        assertEquals("laura", found.usuarioNombre());
    }

    @Test
    void update_shouldReplaceAgendaData() {
        Usuario usuario = usuario(1, "laura");
        when(agendaRepository.findById(1)).thenReturn(Optional.of(bloque(1, usuario)));
        when(usuarioService.obtener(1)).thenReturn(usuario);
        when(agendaRepository.save(any(Agenda.class))).thenAnswer(inv -> inv.getArgument(0));

        AgendaResponseDTO updated = service.update(1, new AgendaRequestDTO("martes", "08:00", "12:00", 1));

        assertEquals("martes", updated.diasemana());
        assertEquals("08:00", updated.horainicio());
        assertEquals("12:00", updated.horafin());
    }

    @Test
    void delete_shouldRemoveAgendaBlock() {
        Usuario usuario = usuario(1, "laura");
        when(agendaRepository.findById(1)).thenReturn(Optional.of(bloque(1, usuario)));

        service.delete(1);

        verify(agendaRepository).delete(any(Agenda.class));
    }

    @Test
    void findById_shouldThrowWhenBlockDoesNotExist() {
        when(agendaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }
}
