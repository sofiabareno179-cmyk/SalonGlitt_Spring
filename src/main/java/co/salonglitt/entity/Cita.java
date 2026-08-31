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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fechahora", nullable = false)
    private LocalDateTime fechahora;

    @Column(nullable = false)
    private String estado;

    @Column(columnDefinition = "text")
    private String servicio;

    public Cita() {
    }

    public Cita(Usuario usuario, LocalDateTime fechahora, String estado, String servicio) {
        this.usuario = usuario;
        this.fechahora = fechahora;
        this.estado = estado;
        this.servicio = servicio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechahora() {
        return fechahora;
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

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
}
