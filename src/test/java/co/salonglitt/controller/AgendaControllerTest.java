package co.salonglitt.controller;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.AgendaService;
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

@SuppressWarnings("null")
@WebMvcTest(controllers = AgendaController.class)
class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgendaService agendaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        AgendaResponseDTO dto = new AgendaResponseDTO(1, "lunes", "09:00", "14:00", 1, "laura");
        when(agendaService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].diasemana").value("lunes"))
                .andExpect(jsonPath("$[0].usuarioNombre").value("laura"));
    }

    @Test
    void obtener_shouldReturnDTO_whenAgendaExists() throws Exception {
        AgendaResponseDTO dto = new AgendaResponseDTO(1, "lunes", "09:00", "14:00", 1, "laura");
        when(agendaService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/agendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.horainicio").value("09:00"));
    }

    @Test
    void obtener_shouldReturn404_whenAgendaNotFound() throws Exception {
        when(agendaService.findById(99)).thenThrow(new NotFoundException("Bloque de agenda no encontrado con id 99"));

        mockMvc.perform(get("/api/agendas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        AgendaRequestDTO request = new AgendaRequestDTO("martes", "08:00", "12:00", 1);
        AgendaResponseDTO response = new AgendaResponseDTO(2, "martes", "08:00", "12:00", 1, "laura");
        when(agendaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
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
        AgendaRequestDTO request = new AgendaRequestDTO("miercoles", "10:00", "13:00", 1);
        AgendaResponseDTO response = new AgendaResponseDTO(1, "miercoles", "10:00", "13:00", 1, "laura");
        when(agendaService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/agendas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diasemana").value("miercoles"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(agendaService).delete(1);

        mockMvc.perform(delete("/api/agendas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenAgendaNotFound() throws Exception {
        doThrow(new NotFoundException("Bloque de agenda no encontrado con id 99")).when(agendaService).delete(99);

        mockMvc.perform(delete("/api/agendas/99"))
                .andExpect(status().isNotFound());
    }
}
