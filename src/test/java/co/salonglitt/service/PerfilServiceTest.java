package co.salonglitt.service;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.entity.Perfil;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.PerfilRepository;
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
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PerfilService service;

    private Usuario usuario(Integer id, String nombreuser) {
        Usuario u = new Usuario(nombreuser, nombreuser + "@mail.com", "hash", "123", "cliente");
        u.setId(id);
        return u;
    }

    private Perfil perfil(Integer id, String nombre, Usuario usuario) {
        Perfil p = new Perfil(nombre, "apellido", "bio", usuario);
        p.setId(id);
        return p;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        Usuario usuario = usuario(1, "ana");
        when(usuarioService.obtener(1)).thenReturn(usuario);
        when(perfilRepository.save(any(Perfil.class))).thenAnswer(inv -> {
            Perfil p = inv.getArgument(0);
            p.setId(1);
            return p;
        });

        PerfilResponseDTO created = service.create(new PerfilRequestDTO("Ana", "Gomez", "bio", 1));

        assertNotNull(created.id());
        assertEquals("Ana", created.nombre());
        assertEquals("Gomez", created.apellido());
        assertEquals(1, created.usuarioId());
        assertEquals("ana", created.usuarioNombre());
    }

    @Test
    void findAll_shouldReturnAllProfiles() {
        Usuario u1 = usuario(1, "ana");
        Usuario u2 = usuario(2, "luis");
        when(perfilRepository.findAll()).thenReturn(List.of(
                perfil(1, "Ana", u1),
                perfil(2, "Luis", u2)));

        List<PerfilResponseDTO> perfiles = service.findAll();

        assertEquals(2, perfiles.size());
        assertTrue(perfiles.stream().anyMatch(p -> p.nombre().equals("Ana")));
        assertTrue(perfiles.stream().anyMatch(p -> p.nombre().equals("Luis")));
    }

    @Test
    void findById_shouldReturnSavedProfile() {
        Usuario usuario = usuario(1, "ana");
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil(1, "Ana", usuario)));

        PerfilResponseDTO found = service.findById(1);

        assertEquals(1, found.id());
        assertEquals("Ana", found.nombre());
        assertEquals("ana", found.usuarioNombre());
    }

    @Test
    void update_shouldReplaceProfileData() {
        Usuario usuario = usuario(1, "ana");
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil(1, "Ana", usuario)));
        when(usuarioService.obtener(1)).thenReturn(usuario);
        when(perfilRepository.save(any(Perfil.class))).thenAnswer(inv -> inv.getArgument(0));

        PerfilResponseDTO updated = service.update(1, new PerfilRequestDTO("Ana Maria", "G", "nueva bio", 1));

        assertEquals("Ana Maria", updated.nombre());
        assertEquals("G", updated.apellido());
        assertEquals("nueva bio", updated.bio());
    }

    @Test
    void delete_shouldRemoveProfile() {
        Usuario usuario = usuario(1, "ana");
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil(1, "Ana", usuario)));

        service.delete(1);

        verify(perfilRepository).delete(any(Perfil.class));
    }

    @Test
    void findById_shouldThrowWhenProfileDoesNotExist() {
        when(perfilRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }

    @Test
    void update_shouldThrowWhenProfileDoesNotExist() {
        when(perfilRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(999, new PerfilRequestDTO("Nuevo", "A", "bio", 1)));
    }

    @Test
    void delete_shouldThrowWhenProfileDoesNotExist() {
        when(perfilRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(999));
    }
}
