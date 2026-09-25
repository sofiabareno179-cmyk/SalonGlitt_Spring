package co.salonglitt.controller;

import co.salonglitt.dto.PromocionRequestDTO;
import co.salonglitt.dto.PromocionResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.PromocionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@WebMvcTest(controllers = PromocionController.class)
class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private PromocionService promocionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarActivas_shouldReturnActivas() throws Exception {
        PromocionResponseDTO dto = new PromocionResponseDTO(1L, "2x1", "Verano", 1L, "Corte", null, null,
                new BigDecimal("50"), LocalDate.now(), LocalDate.now().plusDays(5), true);
        when(promocionService.findActivas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/promociones/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("2x1"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        PromocionRequestDTO request = new PromocionRequestDTO("Descuento 10%", "tinte", 1L, null,
                new BigDecimal("10"), LocalDate.now(), LocalDate.now().plusDays(3), true);
        PromocionResponseDTO response = new PromocionResponseDTO(2L, "Descuento 10%", "tinte", 1L, "Tinte",
                null, null, new BigDecimal("10"), LocalDate.now(), LocalDate.now().plusDays(3), true);
        when(promocionService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(promocionService.findById(99L)).thenThrow(new NotFoundException("Promoción no encontrada con id 99"));

        mockMvc.perform(get("/api/promociones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        PromocionRequestDTO request = new PromocionRequestDTO("Oferta", "x", null, 1L,
                new BigDecimal("15"), LocalDate.now(), LocalDate.now().plusDays(1), false);
        PromocionResponseDTO response = new PromocionResponseDTO(1L, "Oferta", "x", null, null, 1L, "Shampoo",
                new BigDecimal("15"), LocalDate.now(), LocalDate.now().plusDays(1), false);
        when(promocionService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/promociones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoNombre").value("Shampoo"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(promocionService).delete(1L);

        mockMvc.perform(delete("/api/promociones/1"))
                .andExpect(status().isNoContent());
    }
}
