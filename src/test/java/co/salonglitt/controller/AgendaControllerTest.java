package co.salonglitt.controller;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(controllers = AgendaController.class)
class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendaService agendaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        AgendaResponseDTO dto = new AgendaResponseDTO(1L, 1L, "Laura", LocalDate.of(2026, 9, 1),
                LocalTime.of(9, 0), LocalTime.of(10, 0), true);
        when(agendaService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estilistaNombre").value("Laura"));
    }

    @Test
    void obtener_shouldReturnDTO_whenBloqueExists() throws Exception {
        AgendaResponseDTO dto = new AgendaResponseDTO(1L, 1L, "Laura", LocalDate.of(2026, 9, 1),
                LocalTime.of(9, 0), LocalTime.of(10, 0), true);
        when(agendaService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/agendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fecha").value("2026-09-01"))
                .andExpect(jsonPath("$.horaInicio").value("09:00:00"));
    }

    @Test
    void obtener_shouldReturn404_whenBloqueNotFound() throws Exception {
        when(agendaService.findById(99L)).thenThrow(new NotFoundException("Bloque de agenda no encontrado con id 99"));

        mockMvc.perform(get("/api/agendas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorEstilista_shouldReturnList() throws Exception {
        AgendaResponseDTO dto = new AgendaResponseDTO(1L, 5L, "Laura", LocalDate.of(2026, 9, 1),
                LocalTime.of(14, 0), LocalTime.of(15, 0), false);
        when(agendaService.findByEstilista(5L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/agendas/estilista/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estilistaId").value(5))
                .andExpect(jsonPath("$[0].disponible").value(false));
    }

    @Test
    void listarPorEstilistaYFecha_shouldReturnList() throws Exception {
        AgendaResponseDTO dto = new AgendaResponseDTO(2L, 5L, "Laura", LocalDate.of(2026, 9, 2),
                LocalTime.of(8, 0), LocalTime.of(12, 0), true);
        when(agendaService.findByEstilistaYFecha(eq(5L), eq(LocalDate.of(2026, 9, 2)))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/agendas/estilista/5")
                        .param("fecha", "2026-09-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        AgendaRequestDTO request = new AgendaRequestDTO(1L, LocalDate.of(2026, 9, 1),
                LocalTime.of(9, 0), LocalTime.of(10, 0), null);
        AgendaResponseDTO response = new AgendaResponseDTO(3L, 1L, "Laura", LocalDate.of(2026, 9, 1),
                LocalTime.of(9, 0), LocalTime.of(10, 0), true);
        when(agendaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void crear_shouldReturn400_whenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        AgendaRequestDTO request = new AgendaRequestDTO(1L, LocalDate.of(2026, 9, 1),
                LocalTime.of(10, 0), LocalTime.of(11, 30), false);
        AgendaResponseDTO response = new AgendaResponseDTO(1L, 1L, "Laura", LocalDate.of(2026, 9, 1),
                LocalTime.of(10, 0), LocalTime.of(11, 30), false);
        when(agendaService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/agendas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disponible").value(false));
    }

    @Test
    void actualizar_shouldReturn404_whenBloqueNotFound() throws Exception {
        AgendaRequestDTO request = new AgendaRequestDTO(1L, LocalDate.of(2026, 9, 1),
                LocalTime.of(10, 0), LocalTime.of(11, 30), null);
        when(agendaService.update(eq(99L), any())).thenThrow(new NotFoundException("Bloque de agenda no encontrado con id 99"));

        mockMvc.perform(put("/api/agendas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(agendaService).delete(1L);

        mockMvc.perform(delete("/api/agendas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenBloqueNotFound() throws Exception {
        doThrow(new NotFoundException("Bloque de agenda no encontrado con id 99")).when(agendaService).delete(99L);

        mockMvc.perform(delete("/api/agendas/99"))
                .andExpect(status().isNotFound());
    }
}