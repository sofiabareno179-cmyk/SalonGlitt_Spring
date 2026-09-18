package co.salonglitt.service;

import co.salonglitt.dto.CatalogoPrecioRequestDTO;
import co.salonglitt.dto.CatalogoPrecioResponseDTO;
import co.salonglitt.entity.CatalogoPrecio;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CatalogoPrecioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoPrecioService {

    private final CatalogoPrecioRepository catalogoPrecioRepository;

    public CatalogoPrecioService(CatalogoPrecioRepository catalogoPrecioRepository) {
        this.catalogoPrecioRepository = catalogoPrecioRepository;
    }

    public List<CatalogoPrecioResponseDTO> findAll() {
        return catalogoPrecioRepository.findAll().stream().map(this::aDto).toList();
    }

    public CatalogoPrecioResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public CatalogoPrecioResponseDTO create(CatalogoPrecioRequestDTO dto) {
        CatalogoPrecio c = new CatalogoPrecio(dto.nombre().trim(), dto.descripcion(), dto.precio(),
                dto.categoria().trim());
        return aDto(catalogoPrecioRepository.save(c));
    }

    public CatalogoPrecioResponseDTO update(Integer id, CatalogoPrecioRequestDTO dto) {
        CatalogoPrecio actual = obtener(id);
        actual.setNombre(dto.nombre().trim());
        actual.setDescripcion(dto.descripcion());
        actual.setPrecio(dto.precio());
        actual.setCategoria(dto.categoria().trim());
        return aDto(catalogoPrecioRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        catalogoPrecioRepository.deleteById(id);
    }

    private CatalogoPrecio obtener(Integer id) {
        return catalogoPrecioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Precio de catálogo no encontrado con id " + id));
    }

    private CatalogoPrecioResponseDTO aDto(CatalogoPrecio c) {
        return new CatalogoPrecioResponseDTO(c.getId(), c.getNombre(), c.getDescripcion(),
                c.getPrecio(), c.getCategoria(), c.getFechaCreacion());
    }
}