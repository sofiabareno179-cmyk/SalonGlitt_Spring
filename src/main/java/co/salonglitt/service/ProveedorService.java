package co.salonglitt.service;

import co.salonglitt.dto.ProveedorRequestDTO;
import co.salonglitt.dto.ProveedorResponseDTO;
import co.salonglitt.entity.Proveedor;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProveedorResponseDTO> findAll() {
        return proveedorRepository.findAll().stream().map(this::aDto).toList();
    }

    public ProveedorResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public ProveedorResponseDTO create(ProveedorRequestDTO dto) {
        Proveedor p = new Proveedor(dto.nombre().trim(), dto.telefono(), dto.email(), dto.direccion(),
                dto.activo() == null || dto.activo());
        return aDto(proveedorRepository.save(p));
    }

    public ProveedorResponseDTO update(Long id, ProveedorRequestDTO dto) {
        Proveedor actual = obtener(id);
        actual.setNombre(dto.nombre().trim());
        actual.setTelefono(dto.telefono());
        actual.setEmail(dto.email());
        actual.setDireccion(dto.direccion());
        actual.setActivo(dto.activo() == null || dto.activo());
        return aDto(proveedorRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        proveedorRepository.deleteById(id);
    }

    private Proveedor obtener(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con id " + id));
    }

    private ProveedorResponseDTO aDto(Proveedor p) {
        return new ProveedorResponseDTO(p.getId(), p.getNombre(), p.getTelefono(), p.getEmail(),
                p.getDireccion(), p.isActivo());
    }
}
