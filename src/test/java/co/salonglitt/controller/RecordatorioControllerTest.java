package co.salonglitt.controller;

import co.salonglitt.dto.RecordatorioRequestDTO;
import co.salonglitt.dto.RecordatorioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.RecordatorioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecordatorioController.class)
class RecordatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecordatorioService recordatorioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarPorUsuario_shouldReturnList() throws Exception {
        RecordatorioResponseDTO dto = new RecordatorioResponseDTO(1, "Tu cita", "Mañana",
                "2026-09-10", 1);
        when(recordatorioService.findByUsuario(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/recordatorios/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Tu cita"))
                .andExpect(jsonPath("$[0].idrecordatorios").value(1))
                .andExpect(jsonPath("$[0].idusuario").value(1));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        RecordatorioRequestDTO request = new RecordatorioRequestDTO("Tu cita", "Mañana", "2026-09-10", 1);
        RecordatorioResponseDTO response = new RecordatorioResponseDTO(2, "Tu cita", "Mañana",
                "2026-09-10", 1);
        when(recordatorioService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/recordatorios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idrecordatorios").value(2));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(recordatorioService.findById(99)).thenThrow(new NotFoundException("Recordatorio no encontrado con id 99"));

        mockMvc.perform(get("/api/recordatorios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(recordatorioService).delete(1);

        mockMvc.perform(delete("/api/recordatorios/1"))
                .andExpect(status().isNoContent());
    }
}