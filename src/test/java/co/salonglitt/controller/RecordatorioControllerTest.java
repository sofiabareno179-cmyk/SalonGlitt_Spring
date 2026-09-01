package co.salonglitt.controller;

import co.salonglitt.dto.RecordatorioRequestDTO;
import co.salonglitt.dto.RecordatorioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.RecordatorioService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
@WebMvcTest(controllers = RecordatorioController.class)
class RecordatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecordatorioService recordatorioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarPorCita_shouldReturnList() throws Exception {
        RecordatorioResponseDTO dto = new RecordatorioResponseDTO(1L, 1L, LocalDateTime.now().plusDays(1),
                "EMAIL", false);
        when(recordatorioService.findByCita(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/recordatorios/cita/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("EMAIL"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        RecordatorioRequestDTO request = new RecordatorioRequestDTO(1L, LocalDateTime.now().plusDays(1), "SMS");
        RecordatorioResponseDTO response = new RecordatorioResponseDTO(2L, 1L, LocalDateTime.now().plusDays(1),
                "SMS", false);
        when(recordatorioService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/recordatorios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void marcarEnviado_shouldReturnEnviado() throws Exception {
        RecordatorioResponseDTO response = new RecordatorioResponseDTO(1L, 1L, LocalDateTime.now(), "EMAIL", true);
        when(recordatorioService.marcarEnviado(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/recordatorios/1/enviado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enviado").value(true));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(recordatorioService.findById(99L)).thenThrow(new NotFoundException("Recordatorio no encontrado con id 99"));

        mockMvc.perform(get("/api/recordatorios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(recordatorioService).delete(1L);

        mockMvc.perform(delete("/api/recordatorios/1"))
                .andExpect(status().isNoContent());
    }
}
