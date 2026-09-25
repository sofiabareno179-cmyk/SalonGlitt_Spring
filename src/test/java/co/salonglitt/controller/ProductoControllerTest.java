package co.salonglitt.controller;

import co.salonglitt.dto.ProductoRequestDTO;
import co.salonglitt.dto.ProductoResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoResponseDTO dto(int id, String nombre) {
        return new ProductoResponseDTO(id, nombre, "Descripcion", 15000.0, "cabello");
    }

    @Test
    void listar_shouldReturnList() throws Exception {
        ProductoResponseDTO dto = dto(1, "Shampoo");
        when(productoService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Shampoo"))
                .andExpect(jsonPath("$[0].categoria").value("cabello"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        ProductoRequestDTO request = new ProductoRequestDTO("Acondicionador", "Hidratante", 12000.0, "cabello");
        ProductoResponseDTO response = dto(2, "Acondicionador");
        when(productoService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(productoService.findById(99)).thenThrow(new NotFoundException("Producto no encontrado con id 99"));

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        ProductoRequestDTO request = new ProductoRequestDTO("Tinte", "Caja", 200.0, "color");
        ProductoResponseDTO response = dto(1, "Tinte");
        when(productoService.update(eq(1), any())).thenReturn(response);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("cabello"));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(productoService).delete(1);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}