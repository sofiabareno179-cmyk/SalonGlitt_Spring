package co.salonglitt.service;

import co.salonglitt.dto.ServicioRequestDTO;
import co.salonglitt.dto.ServicioResponseDTO;
import co.salonglitt.entity.Servicio;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ServicioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioService service;

    private Servicio servicio(Integer id, String nombre) {
        Servicio s = new Servicio(nombre, new BigDecimal("60000"), "60", "tratamiento", null, null);
        s.setId(id);
        return s;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        when(servicioRepository.save(any(Servicio.class))).thenAnswer(inv -> {
            Servicio s = inv.getArgument(0);
            s.setId(1);
            return s;
        });

        ServicioResponseDTO created = service.create(
                new ServicioRequestDTO("Corte", new BigDecimal("60000"), "45", "peluqueria", null, null));

        assertNotNull(created.id());
        assertEquals("Corte", created.nombre());
        assertEquals(new BigDecimal("60000"), created.precio());
        assertEquals("45", created.duracion());
        assertEquals("peluqueria", created.categoria());
    }

    @Test
    void findAll_shouldReturnAllServices() {
        when(servicioRepository.findAll()).thenReturn(List.of(servicio(1, "Corte")));

        List<ServicioResponseDTO> servicios = service.findAll();

        assertEquals(1, servicios.size());
        assertTrue(servicios.stream().anyMatch(s -> s.nombre().equals("Corte")));
    }

    @Test
    void findById_shouldReturnSavedService() {
        when(servicioRepository.findById(1)).thenReturn(Optional.of(servicio(1, "Peinado")));

        ServicioResponseDTO found = service.findById(1);

        assertEquals(1, found.id());
        assertEquals("Peinado", found.nombre());
    }

    @Test
    void update_shouldReplaceServiceData() {
        Servicio existente = servicio(1, "Lavado");
        when(servicioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(servicioRepository.save(any(Servicio.class))).thenAnswer(inv -> inv.getArgument(0));

        ServicioResponseDTO updated = service.update(1,
                new ServicioRequestDTO("Tintura", new BigDecimal("90000"), "90", "color", null, null));

        assertEquals("Tintura", updated.nombre());
        assertEquals(new BigDecimal("90000"), updated.precio());
        assertEquals("90", updated.duracion());
        assertEquals("color", updated.categoria());
    }

    @Test
    void delete_shouldRemoveService() {
        when(servicioRepository.findById(1)).thenReturn(Optional.of(servicio(1, "Depilación")));

        service.delete(1);

        verify(servicioRepository).deleteById(1);
    }

    @Test
    void findById_shouldThrowWhenServiceDoesNotExist() {
        when(servicioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }

    @Test
    void update_shouldThrowWhenServiceDoesNotExist() {
        when(servicioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(999,
                new ServicioRequestDTO("x", new BigDecimal("20"), "10", "c", null, null)));
    }

    @Test
    void delete_shouldThrowWhenServiceDoesNotExist() {
        when(servicioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(999));
    }
}