package co.salonglitt.controller;

import co.salonglitt.dto.UsuarioRequestDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1, "ana", "ana@mail.com", "3001234567", "cliente");
        when(usuarioService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombreuser").value("ana"))
                .andExpect(jsonPath("$[0].rol").value("cliente"));
    }

    @Test
    void obtener_shouldReturnDTO_whenUsuarioExists() throws Exception {
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1, "ana", "ana@mail.com", "3001234567", "cliente");
        when(usuarioService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@mail.com"));
    }

    @Test
    void obtener_shouldReturn404_whenUsuarioNotFound() throws Exception {
        when(usuarioService.findById(99)).thenThrow(new NotFoundException("Usuario no encontrado con id 99"));

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO("ana", "ana@mail.com", "hash", "3001234567", "cliente");
        UsuarioResponseDTO response = new UsuarioResponseDTO(2, "ana", "ana@mail.com", "3001234567", "cliente");
        when(usuarioService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void crear_shouldReturn400_whenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO("ana", "anag@mail.com", "hash", "3009876543", "estilista");
        UsuarioResponseDTO response = new UsuarioResponseDTO(1, "ana", "anag@mail.com", "3009876543", "estilista");
        when(usuarioService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("estilista"));
    }

    @Test
    void actualizar_shouldReturn404_whenUsuarioNotFound() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO("nope", "nope@mail.com", "hash", null, "cliente");
        when(usuarioService.update(eq(99), any())).thenThrow(new NotFoundException("Usuario no encontrado con id 99"));

        mockMvc.perform(put("/api/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(usuarioService).delete(1);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenUsuarioNotFound() throws Exception {
        doThrow(new NotFoundException("Usuario no encontrado con id 99")).when(usuarioService).delete(99);

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}
