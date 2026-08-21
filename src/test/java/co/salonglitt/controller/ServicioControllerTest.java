package co.salonglitt.controller;

import co.salonglitt.dto.ServicioRequestDTO;
import co.salonglitt.dto.ServicioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.ServicioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(controllers = ServicioController.class)
class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioService servicioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        ServicioResponseDTO dto = new ServicioResponseDTO(1L, "Corte", "Corte clásico", new BigDecimal("25000"), 30, true);
        when(servicioService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Corte"));
    }

    @Test
    void obtener_shouldReturnDTO_whenServicioExists() throws Exception {
        ServicioResponseDTO dto = new ServicioResponseDTO(1L, "Corte", "Corte clásico", new BigDecimal("25000"), 30, true);
        when(servicioService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/servicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duracionMinutos").value(30));
    }

    @Test
    void obtener_shouldReturn404_whenServicioNotFound() throws Exception {
        when(servicioService.findById(99L)).thenThrow(new NotFoundException("Servicio no encontrado con id 99"));

        mockMvc.perform(get("/api/servicios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        ServicioRequestDTO request = new ServicioRequestDTO("Tinte", "Tinte completo", new BigDecimal("80000"), 90, true);
        ServicioResponseDTO response = new ServicioResponseDTO(2L, "Tinte", "Tinte completo", new BigDecimal("80000"), 90, true);
        when(servicioService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void crear_shouldReturn400_whenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crear_shouldReturn400_whenPriceNegative() throws Exception {
        String body = "{\"nombre\":\"Tinte\",\"precio\":-5,\"duracionMinutos\":90}";

        mockMvc.perform(post("/api/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        ServicioRequestDTO request = new ServicioRequestDTO("Corte Premium", "Con lavado", new BigDecimal("40000"), 45, true);
        ServicioResponseDTO response = new ServicioResponseDTO(1L, "Corte Premium", "Con lavado", new BigDecimal("40000"), 45, true);
        when(servicioService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/servicios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Corte Premium"));
    }

    @Test
    void actualizar_shouldReturn404_whenServicioNotFound() throws Exception {
        ServicioRequestDTO request = new ServicioRequestDTO("Nope", "No existe", new BigDecimal("100"), 10, true);
        when(servicioService.update(eq(99L), any())).thenThrow(new NotFoundException("Servicio no encontrado con id 99"));

        mockMvc.perform(put("/api/servicios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(servicioService).delete(1L);

        mockMvc.perform(delete("/api/servicios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenServicioNotFound() throws Exception {
        doThrow(new NotFoundException("Servicio no encontrado con id 99")).when(servicioService).delete(99L);

        mockMvc.perform(delete("/api/servicios/99"))
                .andExpect(status().isNotFound());
    }
}