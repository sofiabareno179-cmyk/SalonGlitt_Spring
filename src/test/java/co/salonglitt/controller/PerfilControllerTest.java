package co.salonglitt.controller;

import co.salonglitt.dto.PerfilRequestDTO;
import co.salonglitt.dto.PerfilResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.PerfilService;
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

@WebMvcTest(controllers = PerfilController.class)
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PerfilService perfilService;

    @Autowired
    private ObjectMapper objectMapper;

    private PerfilResponseDTO dto(int id, String nombre) {
        return new PerfilResponseDTO(id, nombre, "Perez", "bio", 1, "Ana");
    }

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        PerfilResponseDTO dto = dto(1, "Ana");
        when(perfilService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/perfiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void obtener_shouldReturnDTO_whenPerfilExists() throws Exception {
        PerfilResponseDTO dto = dto(1, "Ana");
        when(perfilService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/perfiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("bio"));
    }

    @Test
    void obtener_shouldReturn404_whenPerfilNotFound() throws Exception {
        when(perfilService.findById(99)).thenThrow(new NotFoundException("Perfil no encontrado con id 99"));

        mockMvc.perform(get("/api/perfiles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        PerfilRequestDTO request = new PerfilRequestDTO("Ana", "Perez", "bio", 1);
        PerfilResponseDTO response = dto(2, "Ana");
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
        PerfilRequestDTO request = new PerfilRequestDTO("Ana Maria", "Perez", "nuevo", 1);
        PerfilResponseDTO response = dto(1, "Ana Maria");
        when(perfilService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/perfiles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana Maria"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(perfilService).delete(1);

        mockMvc.perform(delete("/api/perfiles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenPerfilNotFound() throws Exception {
        doThrow(new NotFoundException("Perfil no encontrado con id 99")).when(perfilService).delete(99);

        mockMvc.perform(delete("/api/perfiles/99"))
                .andExpect(status().isNotFound());
    }
}