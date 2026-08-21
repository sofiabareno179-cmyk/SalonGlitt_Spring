package co.salonglitt.controller;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.PerfilService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(controllers = PerfilController.class)
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfilService perfilService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        PerfilResponseDTO dto = new PerfilResponseDTO(1L, "ADMIN", "Administrador");
        when(perfilService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/perfiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("ADMIN"));
    }

    @Test
    void obtener_shouldReturnDTO_whenPerfilExists() throws Exception {
        PerfilResponseDTO dto = new PerfilResponseDTO(1L, "ADMIN", "Administrador");
        when(perfilService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/perfiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtener_shouldReturn404_whenPerfilNotFound() throws Exception {
        when(perfilService.findById(99L)).thenThrow(new NotFoundException("Perfil no encontrado con id 99"));

        mockMvc.perform(get("/api/perfiles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        PerfilRequestDTO request = new PerfilRequestDTO("CLIENTE", "Cliente del salón");
        PerfilResponseDTO response = new PerfilResponseDTO(2L, "CLIENTE", "Cliente del salón");
        when(perfilService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/perfiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void crear_shouldReturn400_whenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/perfiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        PerfilRequestDTO request = new PerfilRequestDTO("ESTILISTA", "Actualizado");
        PerfilResponseDTO response = new PerfilResponseDTO(1L, "ESTILISTA", "Actualizado");
        when(perfilService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/perfiles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ESTILISTA"));
    }

    @Test
    void actualizar_shouldReturn404_whenPerfilNotFound() throws Exception {
        PerfilRequestDTO request = new PerfilRequestDTO("NOPE", "No existe");
        when(perfilService.update(eq(99L), any())).thenThrow(new NotFoundException("Perfil no encontrado con id 99"));

        mockMvc.perform(put("/api/perfiles/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(perfilService).delete(1L);

        mockMvc.perform(delete("/api/perfiles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenPerfilNotFound() throws Exception {
        doThrow(new NotFoundException("Perfil no encontrado con id 99")).when(perfilService).delete(99L);

        mockMvc.perform(delete("/api/perfiles/99"))
                .andExpect(status().isNotFound());
    }
}