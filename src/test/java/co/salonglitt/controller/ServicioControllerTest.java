package co.salonglitt.controller;

import co.salonglitt.dto.ServicioRequestDTO;
import co.salonglitt.dto.ServicioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.ServicioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ServicioController.class)
class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicioService servicioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        ServicioResponseDTO dto = new ServicioResponseDTO(1, "Corte", new BigDecimal("60000"), "45", "peluqueria", null, null);
        when(servicioService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Corte"))
                .andExpect(jsonPath("$[0].categoria").value("peluqueria"));
    }

    @Test
    void obtener_shouldReturnDTO_whenServicioExists() throws Exception {
        ServicioResponseDTO dto = new ServicioResponseDTO(1, "Corte", new BigDecimal("60000"), "45", "peluqueria", null, null);
        when(servicioService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/servicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio").value(60000));
    }

    @Test
    void obtener_shouldReturn404_whenServicioNotFound() throws Exception {
        when(servicioService.findById(99)).thenThrow(new NotFoundException("Servicio no encontrado con id 99"));

        mockMvc.perform(get("/api/servicios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        ServicioRequestDTO request = new ServicioRequestDTO("Corte", new BigDecimal("60000"), "45", "peluqueria", null, null);
        ServicioResponseDTO response = new ServicioResponseDTO(2, "Corte", new BigDecimal("60000"), "45", "peluqueria", null, null);
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
    void actualizar_shouldReturnDTO() throws Exception {
        ServicioRequestDTO request = new ServicioRequestDTO("Tintura", new BigDecimal("90000"), "90", "color", null, null);
        ServicioResponseDTO response = new ServicioResponseDTO(1, "Tintura", new BigDecimal("90000"), "90", "color", null, null);
        when(servicioService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/servicios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duracion").value("90"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(servicioService).delete(1);

        mockMvc.perform(delete("/api/servicios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenServicioNotFound() throws Exception {
        doThrow(new NotFoundException("Servicio no encontrado con id 99")).when(servicioService).delete(99);

        mockMvc.perform(delete("/api/servicios/99"))
                .andExpect(status().isNotFound());
    }
}
