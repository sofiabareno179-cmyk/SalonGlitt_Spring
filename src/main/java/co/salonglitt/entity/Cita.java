package co.salonglitt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    public Cita() {
    }

    public Cita(Usuario cliente, Servicio servicio, LocalDateTime fechaHora, String estado) {
        this.cliente = cliente;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
