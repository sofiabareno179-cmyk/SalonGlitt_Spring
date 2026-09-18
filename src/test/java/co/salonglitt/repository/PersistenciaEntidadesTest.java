package co.salonglitt.repository;

import co.salonglitt.entity.Agenda;
import co.salonglitt.entity.Bloqueo;
import co.salonglitt.entity.CatalogoPrecio;
import co.salonglitt.entity.Cita;
import co.salonglitt.entity.Inventario;
import co.salonglitt.entity.Notificacion;
import co.salonglitt.entity.Perfil;
import co.salonglitt.entity.Producto;
import co.salonglitt.entity.Promocion;
import co.salonglitt.entity.Proveedor;
import co.salonglitt.entity.Recordatorio;
import co.salonglitt.entity.Servicio;
import co.salonglitt.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersistenciaEntidadesTest {

    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private BloqueoRepository bloqueoRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private CatalogoPrecioRepository catalogoPrecioRepository;
    @Autowired
    private PromocionRepository promocionRepository;
    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Test
    void debieraPersistirTodasLasEntidadesYRelaciones() {
        Usuario clienteU = usuarioRepository.save(new Usuario("Ana", "ana@mail.com", "123", "300111", "cliente"));
        Usuario estilista = usuarioRepository.save(new Usuario("Laura", "laura@mail.com", "123", "300222", "estilista"));

        perfilRepository.save(new Perfil("Ana", "Perez", "Cliente del salón", clienteU));
        perfilRepository.save(new Perfil("Laura", "Gomez", "Estilista", estilista));

        servicioRepository.save(new Servicio("Corte", new BigDecimal("25000"), "45", "peluqueria", null, null));

        citaRepository.save(new Cita(clienteU, LocalDateTime.now().plusDays(5), "PENDIENTE", "Corte"));

        agendaRepository.save(new Agenda("lunes", "09:00", "14:00", estilista));

        bloqueoRepository.save(new Bloqueo(LocalDate.now().plusDays(2), "08:00", "10:00",
                "Vacaciones", estilista));

        proveedorRepository.save(new Proveedor("Distribuidora X", "Juan Pérez", "300333",
                "x@mail.com", "Centro"));

        Producto producto = productoRepository.save(new Producto("Shampoo", "Para cabello", 15000.0, "cabello"));

        inventarioRepository.save(new Inventario(20, LocalDate.now().toString(), producto, null));

        catalogoPrecioRepository.save(new CatalogoPrecio("Corte ejecutivo",
                "Corte con lavado", 28000.0, "peluqueria"));

        promocionRepository.save(new Promocion("2x1 corte", "Verano", true));

        notificacionRepository.save(new Notificacion(clienteU, "Hola", "Bienvenida", false, null));

        recordatorioRepository.save(new Recordatorio("Tu cita", "Mañana tienes una cita",
                LocalDate.now().plusDays(1).toString(), clienteU));

        assertThat(perfilRepository.findAll()).hasSize(2);
        assertThat(usuarioRepository.findAll()).hasSize(2);
        assertThat(citaRepository.findByUsuarioId(clienteU.getId())).hasSize(1);
        assertThat(agendaRepository.findByUsuarioId(estilista.getId())).hasSize(1);
        assertThat(bloqueoRepository.findByUsuarioId(estilista.getId())).hasSize(1);
        assertThat(inventarioRepository.findByProductoId(producto.getId())).isPresent();
        assertThat(promocionRepository.findByActivaTrue()).hasSize(1);
        assertThat(notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(clienteU.getId())).hasSize(1);
        assertThat(recordatorioRepository.findByUsuarioId(clienteU.getId())).hasSize(1);

        assertThat(usuarioRepository.findByEmailIgnoreCase("ana@mail.com")).isPresent();
    }
}