package co.salonglitt.controller;

import co.salonglitt.dto.ProductoRequestDTO;
import co.salonglitt.dto.ProductoResponseDTO;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturnList() throws Exception {
        ProductoResponseDTO dto = new ProductoResponseDTO(1L, "Shampoo", "Anticaspa",
                new BigDecimal("15000"), 1L, "Proveedor X", true);
        when(productoService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Shampoo"))
                .andExpect(jsonPath("$[0].proveedorNombre").value("Proveedor X"));
    }

    @Test
    void crear_shouldReturnCreated() throws Exception {
        ProductoRequestDTO request = new ProductoRequestDTO("Acondicionador", "Hidratante",
                new BigDecimal("12000"), 1L, true);
        ProductoResponseDTO response = new ProductoResponseDTO(2L, "Acondicionador", "Hidratante",
                new BigDecimal("12000"), 1L, "Proveedor X", true);
        when(productoService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void obtener_shouldReturn404_whenNotFound() throws Exception {
        when(productoService.findById(99L)).thenThrow(new NotFoundException("Producto no encontrado con id 99"));

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_shouldReturnDTO() throws Exception {
        ProductoRequestDTO request = new ProductoRequestDTO("Tinte", "Caja", new BigDecimal("200"),
                1L, false);
        ProductoResponseDTO response = new ProductoResponseDTO(1L, "Tinte", "Caja", new BigDecimal("200"),
                1L, "Proveedor X", false);
        when(productoService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void eliminar_shouldReturnNoContent() throws Exception {
        doNothing().when(productoService).delete(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}
