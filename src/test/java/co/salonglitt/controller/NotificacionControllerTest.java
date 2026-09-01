package co.salonglitt.controller;

import co.salonglitt.dto.NotificacionRequestDTO;
import co.salonglitt.dto.NotificacionResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.NotificacionService;
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
@WebMvcTest(controllers = NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarPorUsuario_shouldReturnList() throws Exception {
        NotificacionResponseDTO dto = new NotificacionResponseDTO(1L, 1L, "Hola", "Bienvenida",
                false, LocalDateTime.now());
        when(notificacionService.findByUsuario(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/notificaciones/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Hola"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        NotificacionRequestDTO request = new NotificacionRequestDTO(1L, "Promo", "Nueva promo");
        NotificacionResponseDTO response = new NotificacionResponseDTO(2L, 1L, "Promo", "Nueva promo",
                false, LocalDateTime.now());
        when(notificacionService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void marcarLeida_shouldReturnLeida() throws Exception {
        NotificacionResponseDTO response = new NotificacionResponseDTO(1L, 1L, "Hola", "Mensaje",
                true, LocalDateTime.now());
        when(notificacionService.marcarLeida(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/notificaciones/1/leida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leida").value(true));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(notificacionService.findById(99L)).thenThrow(new NotFoundException("Notificación no encontrada con id 99"));

        mockMvc.perform(get("/api/notificaciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(notificacionService).delete(1L);

        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isNoContent());
    }
}
