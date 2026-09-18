package co.salonglitt.service;

import co.salonglitt.dto.UsuarioRequestDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService service;

    private Usuario user(Integer id, String nombreuser, String rol) {
        Usuario u = new Usuario(nombreuser, nombreuser + "@mail.com", "hash", "3000000000", rol);
        u.setId(id);
        return u;
    }

    @Test
    void create_shouldPersistAndReturnDto() {
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1);
            return u;
        });

        UsuarioResponseDTO created = service.create(
                new UsuarioRequestDTO("laura", "laura@mail.com", "hash", "3000000000", "cliente"));

        assertNotNull(created.id());
        assertEquals("laura", created.nombreuser());
        assertEquals("laura@mail.com", created.email());
        assertEquals("cliente", created.rol());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        when(usuarioRepository.findAll()).thenReturn(List.of(
                user(1, "ana", "cliente"),
                user(2, "luis", "admin")));

        List<UsuarioResponseDTO> usuarios = service.findAll();

        assertEquals(2, usuarios.size());
        assertTrue(usuarios.stream().anyMatch(u -> u.nombreuser().equals("ana")));
        assertTrue(usuarios.stream().anyMatch(u -> u.nombreuser().equals("luis")));
    }

    @Test
    void findById_shouldReturnSavedUser() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(user(1, "sofia", "cliente")));

        UsuarioResponseDTO found = service.findById(1);

        assertEquals(1, found.id());
        assertEquals("sofia", found.nombreuser());
    }

    @Test
    void findByEmail_shouldReturnSavedUser() {
        when(usuarioRepository.findByEmailIgnoreCase("sofia@mail.com"))
                .thenReturn(Optional.of(user(1, "sofia", "cliente")));

        UsuarioResponseDTO found = service.findByEmail("sofia@mail.com");

        assertEquals("sofia", found.nombreuser());
    }

    @Test
    void update_shouldReplaceUserData() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(user(1, "pedro", "cliente")));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDTO updated = service.update(1,
                new UsuarioRequestDTO("pedro.ruiz", "pedro@mail.com", "hash2", "555", "estilista"));

        assertEquals("pedro.ruiz", updated.nombreuser());
        assertEquals("pedro@mail.com", updated.email());
        assertEquals("estilista", updated.rol());
    }

    @Test
    void delete_shouldRemoveUser() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(user(1, "camila", "cliente")));

        service.delete(1);

        verify(usuarioRepository).deleteById(1);
    }

    @Test
    void findById_shouldThrowWhenUserDoesNotExist() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(999));
    }

    @Test
    void update_shouldThrowWhenUserDoesNotExist() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(999,
                new UsuarioRequestDTO("x", "x@mail.com", "h", "1", "cliente")));
    }

    @Test
    void delete_shouldThrowWhenUserDoesNotExist() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(999));
    }
}