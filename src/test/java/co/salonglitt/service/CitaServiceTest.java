package co.salonglitt.service;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.entity.Cita;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CitaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private CitaService service;

    private Usuario usuario(Integer id, String nombreuser) {
        Usuario u = new Usuario(nombreuser, nombreuser + "@mail.com", "hash", "111", "cliente");
        u.setId(id);
        return u;
    }

    private Cita cita(Integer id, Usuario usuario, LocalDateTime fechaHora, String estado) {
        Cita c = new Cita(usuario, fechaHora, estado, "corte");
        c.setId(id);
        return c;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        Usuario usuario = usuario(1, "ana");
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0).withNano(0);
        when(usuarioService.obtener(1)).thenReturn(usuario);
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> {
            Cita c = inv.getArgument(0);
            c.setId(1);
            return c;
        });

        CitaResponseDTO created = service.create(new CitaRequestDTO(1, fechaHora, null, "corte"));

        assertNotNull(created.id());
        assertEquals(1, created.usuarioId());
        assertEquals("ana", created.usuarioNombre());
        assertEquals("Espera", created.estado());
        assertEquals("corte", created.servicio());
    }

    @Test
    void findByUsuarioAndEstado_shouldFilterAppointments() {
        Usuario usuario = usuario(1, "ana");
        when(usuarioService.obtener(1)).thenReturn(usuario);
        when(citaRepository.findByUsuarioId(1)).thenReturn(List.of(
                cita(1, usuario, LocalDateTime.now().plusDays(2), "Confirmada"),
                cita(2, usuario, LocalDateTime.now().plusDays(3), "Cancelada")));
        when(citaRepository.findByEstadoIgnoreCase("Confirmada")).thenReturn(List.of(
                cita(1, usuario, LocalDateTime.now().plusDays(2), "Confirmada")));

        List<CitaResponseDTO> porUsuario = service.findByUsuario(1);
        List<CitaResponseDTO> porEstado = service.findByEstado("Confirmada");

        assertEquals(2, porUsuario.size());
        assertTrue(porEstado.stream().anyMatch(c -> c.usuarioId().equals(1)));
    }

    @Test
    void update_shouldReplaceAppointmentData() {
        Usuario usuario = usuario(1, "ana");
        LocalDateTime fechaOriginal = LocalDateTime.now().plusDays(5);
        when(citaRepository.findById(1)).thenReturn(Optional.of(cita(1, usuario, fechaOriginal, "Espera")));
        when(usuarioService.obtener(1)).thenReturn(usuario);
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDateTime fechaNueva = fechaOriginal.plusHours(2);
        CitaResponseDTO updated = service.update(1, new CitaRequestDTO(1, fechaNueva, "Confirmada", "liso"));

        assertEquals("Confirmada", updated.estado());
        assertEquals("liso", updated.servicio());
        assertEquals(fechaNueva, updated.fechahora());
    }

    @Test
    void cambiarEstado_shouldUpdateStatus() {
        Usuario usuario = usuario(1, "ana");
        when(citaRepository.findById(1)).thenReturn(Optional.of(cita(1, usuario, LocalDateTime.now().plusDays(6), "Espera")));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaResponseDTO updated = service.cambiarEstado(1, "Cancelada");

        assertEquals("Cancelada", updated.estado());
    }

    @Test
    void delete_shouldRemoveAppointment() {
        Usuario usuario = usuario(1, "ana");
        when(citaRepository.findById(1)).thenReturn(Optional.of(cita(1, usuario, LocalDateTime.now().plusDays(7), "Espera")));

        service.delete(1);

        verify(citaRepository).delete(any(Cita.class));
    }

    @Test
    void findById_shouldThrowWhenAppointmentDoesNotExist() {
        when(citaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }
}
