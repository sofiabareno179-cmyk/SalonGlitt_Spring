package co.salonglitt.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcitas")
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime fechahora;

    @Column(nullable = false, length = 100)
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    @Column(length = 150)
    private String servicio;

    @Column(name = "servicio_legacy", length = 120)
    private String servicioLegacy;

    public Cita() {
    }

    public Cita(Usuario usuario, LocalDateTime fechahora, String estado, String servicio) {
        this.usuario = usuario;
        this.fechahora = fechahora;
        this.estado = estado;
        this.servicio = servicio;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id == null ? null : id.longValue();
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setFechahora(LocalDateTime fechahora) {
        this.fechahora = fechahora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getServicioLegacy() { return servicioLegacy; }
    public void setServicioLegacy(String servicioLegacy) { this.servicioLegacy = servicioLegacy; }
}
