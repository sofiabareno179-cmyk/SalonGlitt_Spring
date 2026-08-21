package co.salonglitt.controller;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.CitaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(controllers = CitaController.class)
class CitaControllerTest {

    private static final LocalDateTime FECHA_FUTURA = LocalDateTime.now().plusDays(7);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaService citaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        CitaResponseDTO dto = new CitaResponseDTO(1L, 1L, "Ana", 1L, "Corte", FECHA_FUTURA, "PENDIENTE");
        when(citaService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clienteNombre").value("Ana"))
                .andExpect(jsonPath("$[0].servicioNombre").value("Corte"));
    }

    @Test
    void listar_shouldFilterByEstado() throws Exception {
        CitaResponseDTO dto = new CitaResponseDTO(1L, 1L, "Ana", 1L, "Corte", FECHA_FUTURA, "CONFIRMADA");
        when(citaService.findByEstado("CONFIRMADA")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/citas")
                        .param("estado", "CONFIRMADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("CONFIRMADA"));
    }

    @Test
    void listarPorCliente_shouldReturnList() throws Exception {
        CitaResponseDTO dto = new CitaResponseDTO(1L, 7L, "Ana", 1L, "Corte", FECHA_FUTURA, "PENDIENTE");
        when(citaService.findByCliente(7L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/citas/cliente/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value(7));
    }

    @Test
    void obtener_shouldReturn404_whenCitaNotFound() throws Exception {
        when(citaService.findById(99L)).thenThrow(new NotFoundException("Cita no encontrada con id 99"));

        mockMvc.perform(get("/api/citas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        CitaRequestDTO request = new CitaRequestDTO(1L, 1L, FECHA_FUTURA, null);
        CitaResponseDTO response = new CitaResponseDTO(2L, 1L, "Ana", 1L, "Corte", FECHA_FUTURA, "PENDIENTE");
        when(citaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void crear_shouldReturn400_whenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crear_shouldReturn400_whenDateInPast() throws Exception {
        CitaRequestDTO request = new CitaRequestDTO(1L, 1L, LocalDateTime.now().minusDays(1), null);

        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cambiarEstado_shouldReturnUpdatedDTO() throws Exception {
        CitaResponseDTO response = new CitaResponseDTO(1L, 1L, "Ana", 1L, "Corte", FECHA_FUTURA, "CONFIRMADA");
        when(citaService.cambiarEstado(1L, "CONFIRMADA")).thenReturn(response);

        mockMvc.perform(patch("/api/citas/1/estado")
                        .param("estado", "CONFIRMADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"));
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        CitaRequestDTO request = new CitaRequestDTO(1L, 2L, FECHA_FUTURA, "CONFIRMADA");
        CitaResponseDTO response = new CitaResponseDTO(1L, 1L, "Ana", 2L, "Tinte", FECHA_FUTURA, "CONFIRMADA");
        when(citaService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.servicioNombre").value("Tinte"));
    }

    @Test
    void actualizar_shouldReturn404_whenCitaNotFound() throws Exception {
        CitaRequestDTO request = new CitaRequestDTO(1L, 1L, FECHA_FUTURA, null);
        when(citaService.update(eq(99L), any())).thenThrow(new NotFoundException("Cita no encontrada con id 99"));

        mockMvc.perform(put("/api/citas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(citaService).delete(1L);

        mockMvc.perform(delete("/api/citas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenCitaNotFound() throws Exception {
        doThrow(new NotFoundException("Cita no encontrada con id 99")).when(citaService).delete(99L);

        mockMvc.perform(delete("/api/citas/99"))
                .andExpect(status().isNotFound());
    }
}