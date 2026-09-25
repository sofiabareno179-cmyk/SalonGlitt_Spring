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

    public ProveedorResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public ProveedorResponseDTO create(ProveedorRequestDTO dto) {
        Proveedor p = new Proveedor(dto.nombreEmpresa().trim(), dto.contactoNombre().trim(),
                dto.telefono(), dto.email(), dto.direccion());
        return aDto(proveedorRepository.save(p));
    }

    public ProveedorResponseDTO update(Integer id, ProveedorRequestDTO dto) {
        Proveedor actual = obtener(id);
        actual.setNombreEmpresa(dto.nombreEmpresa().trim());
        actual.setContactoNombre(dto.contactoNombre().trim());
        actual.setTelefono(dto.telefono());
        actual.setEmail(dto.email());
        actual.setDireccion(dto.direccion());
        return aDto(proveedorRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        proveedorRepository.deleteById(id);
    }

    private Proveedor obtener(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con id " + id));
    }

    private ProveedorResponseDTO aDto(Proveedor p) {
        return new ProveedorResponseDTO(p.getId(), p.getNombreEmpresa(), p.getContactoNombre(),
                p.getTelefono(), p.getEmail(), p.getDireccion());
    }
}