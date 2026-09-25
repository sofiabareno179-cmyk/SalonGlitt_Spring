package co.salonglitt.controller;

import co.salonglitt.dto.BloqueoRequestDTO;
import co.salonglitt.dto.BloqueoResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.BloqueoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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

@WebMvcTest(controllers = BloqueoController.class)
class BloqueoControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private BloqueoService bloqueoService;

    @Autowired
    private ObjectMapper objectMapper;

    private BloqueoResponseDTO dto(int id, String motivo) {
        return new BloqueoResponseDTO(id, 1, "Laura", LocalDate.of(2026, 9, 1),
                "08:00", "10:00", motivo, LocalDateTime.now());
    }

    @Test
    void listar_shouldReturnList() throws Exception {
        BloqueoResponseDTO dto = dto(1, "Vacaciones");
        when(bloqueoService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/bloqueos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].motivo").value("Vacaciones"));
    }

    @Test
    void listarPorUsuario_shouldReturnList() throws Exception {
        BloqueoResponseDTO dto = dto(1, "Vacaciones");
        when(bloqueoService.findByUsuario(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/bloqueos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioNombre").value("Laura"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        BloqueoRequestDTO request = new BloqueoRequestDTO(1, LocalDate.of(2026, 9, 1), "08:00", "10:00", "Descanso");
        BloqueoResponseDTO response = dto(2, "Descanso");
        when(bloqueoService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/bloqueos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(bloqueoService.findById(99)).thenThrow(new NotFoundException("Bloqueo no encontrado con id 99"));

        mockMvc.perform(get("/api/bloqueos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        BloqueoRequestDTO request = new BloqueoRequestDTO(1, LocalDate.of(2026, 9, 2), "08:00", "09:00", "Actualizado");
        BloqueoResponseDTO response = dto(1, "Actualizado");
        when(bloqueoService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/bloqueos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivo").value("Actualizado"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(bloqueoService).delete(1);

        mockMvc.perform(delete("/api/bloqueos/1"))
                .andExpect(status().isNoContent());
    }
}