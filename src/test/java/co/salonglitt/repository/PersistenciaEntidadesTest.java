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
import java.time.LocalTime;

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
        Perfil cliente = perfilRepository.save(new Perfil("CLIENTE", "Cliente del salón"));
        Perfil estilistaPerfil = perfilRepository.save(new Perfil("ESTILISTA", "Estilista"));

        Usuario clienteU = usuarioRepository.save(new Usuario("Ana", "ana@mail.com", "300111", cliente, true));
        Usuario estilista = usuarioRepository.save(new Usuario("Laura", "laura@mail.com", "300222", estilistaPerfil, true));

        Servicio corte = servicioRepository.save(new Servicio("Corte", "Corte clásico", new BigDecimal("25000"), 30, true));

        Cita cita = citaRepository.save(new Cita(clienteU, corte, LocalDateTime.now().plusDays(5), "PENDIENTE"));

        Agenda agenda = agendaRepository.save(new Agenda(estilista, LocalDate.now().plusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0), true));

        Bloqueo bloqueo = bloqueoRepository.save(new Bloqueo(estilista,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2), "Vacaciones"));

        Proveedor proveedor = proveedorRepository.save(new Proveedor("Distribuidora X", "300333", "x@mail.com", "Centro", true));

        Producto producto = productoRepository.save(new Producto("Shampoo", "Para cabello", new BigDecimal("15000"), proveedor, true));

        Inventario inventario = inventarioRepository.save(new Inventario(producto, 20, 5, null));

        CatalogoPrecio catalogo = catalogoPrecioRepository.save(new CatalogoPrecio(corte,
                new BigDecimal("28000"), LocalDate.now(), null));

        Promocion promocion = promocionRepository.save(new Promocion("2x1 corte", "Verano",
                corte, null, new BigDecimal("50"), LocalDate.now(), LocalDate.now().plusDays(10), true));

        Notificacion notificacion = notificacionRepository.save(new Notificacion(clienteU, "Hola", "Bienvenida", false, null));

        Recordatorio recordatorio = recordatorioRepository.save(new Recordatorio(cita, null, "EMAIL", false));

        assertThat(perfilRepository.findAll()).hasSize(2);
        assertThat(usuarioRepository.findAll()).hasSize(2);
        assertThat(citaRepository.findByClienteId(clienteU.getId())).hasSize(1);
        assertThat(agendaRepository.findByEstilistaId(estilista.getId())).hasSize(1);
        assertThat(bloqueoRepository.findByEstilistaId(estilista.getId())).hasSize(1);
        assertThat(productoRepository.findByProveedorId(proveedor.getId())).hasSize(1);
        assertThat(inventarioRepository.findByProductoId(producto.getId())).isPresent();
        assertThat(catalogoPrecioRepository.findByServicioId(corte.getId())).hasSize(1);
        assertThat(promocionRepository.findByActivaTrue()).hasSize(1);
        assertThat(notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(clienteU.getId())).hasSize(1);
        assertThat(recordatorioRepository.findByCitaId(cita.getId())).hasSize(1);

        assertThat(usuarioRepository.findByEmailIgnoreCase("ana@mail.com")).isPresent();
        assertThat(perfilRepository.findByNombreIgnoreCase("cliente")).isPresent();
    }
}
