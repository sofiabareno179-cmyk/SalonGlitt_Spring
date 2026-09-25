package co.salonglitt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "servicio_legacy", length = 120)
    private String servicioLegacy;

    public Cita() {
    }

    public Cita(Usuario cliente, Servicio servicio, LocalDateTime fechaHora, String estado) {
        this.cliente = cliente;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public Cita(Usuario cliente, LocalDateTime fechaHora, String estado, String servicio) {
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.estado = estado == null ? "ESPERA" : estado;
        this.servicioLegacy = servicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id == null ? null : id.longValue();
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getServicioLegacy() { return servicioLegacy; }
    public void setServicioLegacy(String servicioLegacy) { this.servicioLegacy = servicioLegacy; }
}
