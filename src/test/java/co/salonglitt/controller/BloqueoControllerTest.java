package co.salonglitt.controller;

import co.salonglitt.dto.BloqueoRequestDTO;
import co.salonglitt.dto.BloqueoResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.BloqueoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
@WebMvcTest(controllers = BloqueoController.class)
class BloqueoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BloqueoService bloqueoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnList() throws Exception {
        BloqueoResponseDTO dto = new BloqueoResponseDTO(1L, 1L, "Laura",
                LocalDateTime.of(2026, 9, 1, 8, 0), LocalDateTime.of(2026, 9, 1, 10, 0), "Vacaciones");
        when(bloqueoService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/bloqueos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].motivo").value("Vacaciones"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        BloqueoRequestDTO request = new BloqueoRequestDTO(1L,
                LocalDateTime.of(2026, 9, 1, 8, 0), LocalDateTime.of(2026, 9, 1, 10, 0), "Descanso");
        BloqueoResponseDTO response = new BloqueoResponseDTO(2L, 1L, "Laura",
                LocalDateTime.of(2026, 9, 1, 8, 0), LocalDateTime.of(2026, 9, 1, 10, 0), "Descanso");
        when(bloqueoService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/bloqueos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(bloqueoService.findById(99L)).thenThrow(new NotFoundException("Bloqueo no encontrado con id 99"));

        mockMvc.perform(get("/api/bloqueos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        BloqueoRequestDTO request = new BloqueoRequestDTO(1L,
                LocalDateTime.of(2026, 9, 2, 8, 0), LocalDateTime.of(2026, 9, 2, 9, 0), "Actualizado");
        BloqueoResponseDTO response = new BloqueoResponseDTO(1L, 1L, "Laura",
                LocalDateTime.of(2026, 9, 2, 8, 0), LocalDateTime.of(2026, 9, 2, 9, 0), "Actualizado");
        when(bloqueoService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/bloqueos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivo").value("Actualizado"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(bloqueoService).delete(1L);

        mockMvc.perform(delete("/api/bloqueos/1"))
                .andExpect(status().isNoContent());
    }
}
