package co.salonglitt.service;

import co.salonglitt.dto.GaleriaRequestDTO;
import co.salonglitt.dto.GaleriaResponseDTO;
import co.salonglitt.entity.Galeria;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.GaleriaRepository;
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
class GaleriaServiceTest {

    @Mock
    private GaleriaRepository galeriaRepository;

    @InjectMocks
    private GaleriaService service;

    private Galeria item(Integer id, String titulo, String tipo) {
        Galeria g = new Galeria(titulo, "foto.jpg", "desc", LocalDateTime.now(), tipo);
        g.setId(id);
        return g;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        when(galeriaRepository.save(any(Galeria.class))).thenAnswer(inv -> {
            Galeria g = inv.getArgument(0);
            g.setId(1);
            return g;
        });

        GaleriaResponseDTO created = service.create(new GaleriaRequestDTO("Mechas", "bala.jpg", "desc", null, "imagen"));

        assertNotNull(created.id());
        assertEquals("Mechas", created.titulo());
        assertEquals("bala.jpg", created.archivo());
        assertEquals("imagen", created.tipo());
        assertNotNull(created.fechaSubida());
    }

    @Test
    void create_shouldDefaultTipoToImagen() {
        when(galeriaRepository.save(any(Galeria.class))).thenAnswer(inv -> {
            Galeria g = inv.getArgument(0);
            g.setId(1);
            return g;
        });

        GaleriaResponseDTO created = service.create(new GaleriaRequestDTO("Mechas", "bala.jpg", "desc", null, "  "));

        assertEquals("imagen", created.tipo());
    }

    @Test
    void findAll_shouldReturnAllItems() {
        when(galeriaRepository.findAll()).thenReturn(List.of(
                item(1, "Mechas", "imagen"),
                item(2, "Corte", "video")));

        List<GaleriaResponseDTO> items = service.findAll();

        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(g -> g.titulo().equals("Mechas")));
    }

    @Test
    void findByTipo_shouldFilter() {
        when(galeriaRepository.findByTipoIgnoreCase("imagen")).thenReturn(List.of(item(1, "Mechas", "imagen")));

        List<GaleriaResponseDTO> items = service.findByTipo("imagen");

        assertEquals(1, items.size());
        assertEquals("imagen", items.get(0).tipo());
    }

    @Test
    void findById_shouldReturnSavedItem() {
        when(galeriaRepository.findById(1)).thenReturn(Optional.of(item(1, "Mechas", "imagen")));

        GaleriaResponseDTO found = service.findById(1);

        assertEquals(1, found.id());
        assertEquals("Mechas", found.titulo());
    }

    @Test
    void update_shouldReplaceItemData() {
        when(galeriaRepository.findById(1)).thenReturn(Optional.of(item(1, "Mechas", "imagen")));
        when(galeriaRepository.save(any(Galeria.class))).thenAnswer(inv -> inv.getArgument(0));

        GaleriaResponseDTO updated = service.update(1, new GaleriaRequestDTO("Tinte", "nueva.jpg", "nueva", null, "video"));

        assertEquals("Tinte", updated.titulo());
        assertEquals("nueva.jpg", updated.archivo());
        assertEquals("video", updated.tipo());
    }

    @Test
    void delete_shouldRemoveItem() {
        when(galeriaRepository.findById(1)).thenReturn(Optional.of(item(1, "Mechas", "imagen")));

        service.delete(1);

        verify(galeriaRepository).delete(any(Galeria.class));
    }

    @Test
    void findById_shouldThrowWhenItemDoesNotExist() {
        when(galeriaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }
}
