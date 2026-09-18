package co.salonglitt.controller;

import co.salonglitt.dto.GaleriaRequestDTO;
import co.salonglitt.dto.GaleriaResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.GaleriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GaleriaController.class)
class GaleriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GaleriaService galeriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        GaleriaResponseDTO dto = new GaleriaResponseDTO(1, "Mechas", "bala.jpg", "desc", LocalDateTime.now(), "imagen");
        when(galeriaService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/galeria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Mechas"))
                .andExpect(jsonPath("$[0].tipo").value("imagen"));
    }

    @Test
    void listar_shouldFilterByTipo() throws Exception {
        GaleriaResponseDTO dto = new GaleriaResponseDTO(1, "Mechas", "bala.jpg", "desc", LocalDateTime.now(), "imagen");
        when(galeriaService.findByTipo("imagen")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/galeria").param("tipo", "imagen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("imagen"));
    }

    @Test
    void obtener_shouldReturnDTO_whenItemExists() throws Exception {
        GaleriaResponseDTO dto = new GaleriaResponseDTO(1, "Mechas", "bala.jpg", "desc", LocalDateTime.now(), "imagen");
        when(galeriaService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/galeria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.archivo").value("bala.jpg"));
    }

    @Test
    void obtener_shouldReturn404_whenItemNotFound() throws Exception {
        when(galeriaService.findById(99)).thenThrow(new NotFoundException("Elemento de galería no encontrado con id 99"));

        mockMvc.perform(get("/api/galeria/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        GaleriaRequestDTO request = new GaleriaRequestDTO("Mechas", "bala.jpg", "desc", null, "imagen");
        GaleriaResponseDTO response = new GaleriaResponseDTO(2, "Mechas", "bala.jpg", "desc", LocalDateTime.now(), "imagen");
        when(galeriaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/galeria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void crear_shouldReturn400_whenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/galeria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        GaleriaRequestDTO request = new GaleriaRequestDTO("Tinte", "nueva.jpg", "nueva", null, "video");
        GaleriaResponseDTO response = new GaleriaResponseDTO(1, "Tinte", "nueva.jpg", "nueva", LocalDateTime.now(), "video");
        when(galeriaService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/galeria/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("video"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(galeriaService).delete(1);

        mockMvc.perform(delete("/api/galeria/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_shouldReturn404_whenItemNotFound() throws Exception {
        doThrow(new NotFoundException("Elemento de galería no encontrado con id 99")).when(galeriaService).delete(99);

        mockMvc.perform(delete("/api/galeria/99"))
                .andExpect(status().isNotFound());
    }
}
