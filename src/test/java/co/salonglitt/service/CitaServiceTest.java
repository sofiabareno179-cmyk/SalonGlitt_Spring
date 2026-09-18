package co.salonglitt.service;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.entity.Cita;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CitaRepository;
import co.salonglitt.repository.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CitaService service;

    private Usuario usuario(Integer id, String nombre) {
        Usuario u = new Usuario(nombre, nombre + "@mail.com", "123", "300000", "cliente");
        u.setId(id);
        return u;
    }

    private Cita cita(Integer id, Usuario cliente, LocalDateTime fechaHora, String estado) {
        Cita c = new Cita(cliente, fechaHora, estado, "Corte");
        c.setId(id);
        return c;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        Usuario cliente = usuario(1, "ana");
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> {
            Cita c = inv.getArgument(0);
            c.setId(1);
            return c;
        });

        CitaResponseDTO created = service.create(new CitaRequestDTO(1, fechaHora, null, "Corte"));

        assertNotNull(created.id());
        assertEquals(1, created.idusuario());
        assertEquals("PENDIENTE", created.estado());
    }

    @Test
    void findByUsuario_shouldReturnAppointments() {
        Usuario cliente = usuario(1, "ana");
        when(citaRepository.findByUsuarioId(1)).thenReturn(List.of(
                cita(1, cliente, LocalDateTime.now().plusDays(2), "CONFIRMADA"),
                cita(2, cliente, LocalDateTime.now().plusDays(3), "CANCELADA")));

        List<CitaResponseDTO> porUsuario = service.findByUsuario(1);

        assertEquals(2, porUsuario.size());
    }

    @Test
    void findByUsuario_shouldThrowWhenUserDoesNotExist() {
        when(citaRepository.findByUsuarioId(99)).thenReturn(List.of());
        when(usuarioRepository.existsById(99)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.findByUsuario(99));
    }

    @Test
    void findByEstado_shouldReturnAppointmentsByStatus() {
        Usuario cliente = usuario(1, "ana");
        when(citaRepository.findByEstadoIgnoreCase("CONFIRMADA")).thenReturn(List.of(
                cita(1, cliente, LocalDateTime.now().plusDays(2), "CONFIRMADA")));

        List<CitaResponseDTO> porEstado = service.findByEstado("CONFIRMADA");

        assertEquals(1, porEstado.size());
    }

    @Test
    void delete_shouldRemoveAppointment() {
        Usuario cliente = usuario(1, "ana");
        when(citaRepository.findById(1)).thenReturn(
                Optional.of(cita(1, cliente, LocalDateTime.now().plusDays(1), "PENDIENTE")));

        service.delete(1);

        verify(citaRepository).deleteById(1);
    }

    @Test
    void findById_shouldThrowWhenAppointmentDoesNotExist() {
        when(citaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }
}