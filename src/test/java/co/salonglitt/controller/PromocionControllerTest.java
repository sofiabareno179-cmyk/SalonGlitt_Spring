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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

@WebMvcTest(controllers = PromocionController.class)
class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private PromocionService promocionService;

    @Autowired
    private ObjectMapper objectMapper;

    private PromocionResponseDTO dto(int id, String titulo, boolean activa) {
        return new PromocionResponseDTO(id, titulo, "Descripcion", activa, LocalDateTime.now());
    }

    @Test
    void listarActivas_shouldReturnActivas() throws Exception {
        PromocionResponseDTO dto = dto(1, "2x1", true);
        when(promocionService.findActivas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/promociones/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("2x1"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        PromocionRequestDTO request = new PromocionRequestDTO("Descuento 10%", "tinte", true);
        PromocionResponseDTO response = dto(2, "Descuento 10%", true);
        when(promocionService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(promocionService.findById(99)).thenThrow(new NotFoundException("Promoción no encontrada con id 99"));

        mockMvc.perform(get("/api/promociones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        PromocionRequestDTO request = new PromocionRequestDTO("Oferta", "x", false);
        PromocionResponseDTO response = dto(1, "Oferta", false);
        when(promocionService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/promociones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activa").value(false));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(promocionService).delete(1);

        mockMvc.perform(delete("/api/promociones/1"))
                .andExpect(status().isNoContent());
    }
}