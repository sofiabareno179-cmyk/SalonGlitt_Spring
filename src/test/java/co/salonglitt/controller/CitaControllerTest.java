package co.salonglitt.controller;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.CitaService;
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

@SuppressWarnings("null")
@WebMvcTest(controllers = CitaController.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CitaService citaService;

    @Autowired
    private ObjectMapper objectMapper;

    private final LocalDateTime futuro = LocalDateTime.now().plusDays(2);

    @Test
    void listar_shouldReturnListOfDTOs() throws Exception {
        CitaResponseDTO dto = new CitaResponseDTO(1, 1, "ana", futuro, "Espera", "corte");
        when(citaService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].usuarioNombre").value("ana"))
                .andExpect(jsonPath("$[0].estado").value("Espera"));
    }

    @Test
    void listar_shouldFilterByEstado() throws Exception {
        CitaResponseDTO dto = new CitaResponseDTO(1, 1, "ana", futuro, "Confirmada", "corte");
        when(citaService.findByEstado("Confirmada")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/citas").param("estado", "Confirmada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Confirmada"));
    }

    @Test
    void obtener_shouldReturnDTO_whenCitaExists() throws Exception {
        CitaResponseDTO dto = new CitaResponseDTO(1, 1, "ana", futuro, "Espera", "corte");
        when(citaService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/citas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.servicio").value("corte"));
    }

    @Test
    void obtener_shouldReturn404_whenCitaNotFound() throws Exception {
        when(citaService.findById(99)).thenThrow(new NotFoundException("Cita no encontrada con id 99"));

        mockMvc.perform(get("/api/citas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorUsuario_shouldReturnDTOs() throws Exception {
        CitaResponseDTO dto = new CitaResponseDTO(1, 1, "ana", futuro, "Espera", "corte");
        when(citaService.findByUsuario(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/citas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1));
    }

    @Test
    void crear_shouldReturnCreatedStatus() throws Exception {
        CitaRequestDTO request = new CitaRequestDTO(1, futuro, "Espera", "corte");
        CitaResponseDTO response = new CitaResponseDTO(2, 1, "ana", futuro, "Espera", "corte");
        when(citaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void crear_shouldReturn400_whenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cambiarEstado_shouldReturnDTO() throws Exception {
        CitaResponseDTO response = new CitaResponseDTO(1, 1, "ana", futuro, "Cancelada", "corte");
        when(citaService.cambiarEstado(1, "Cancelada")).thenReturn(response);

        mockMvc.perform(patch("/api/citas/1/estado").param("estado", "Cancelada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Cancelada"));
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        CitaRequestDTO request = new CitaRequestDTO(1, futuro, "Confirmada", "liso");
        CitaResponseDTO response = new CitaResponseDTO(1, 1, "ana", futuro, "Confirmada", "liso");
        when(citaService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.servicio").value("liso"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(citaService).delete(1);

        mockMvc.perform(delete("/api/citas/1"))
                .andExpect(status().isNoContent());
    }
}
