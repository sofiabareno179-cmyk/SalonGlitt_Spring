package co.salonglitt.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueos")
public class Bloqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbloqueo")
    private Integer id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false, length = 5)
    private String horaInicio;

    @Column(name = "hora_fin", nullable = false, length = 5)
    private String horaFin;

    @Column(length = 255)
    private String motivo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Bloqueo() {
    }

    public Bloqueo(LocalDate fecha, String horaInicio, String horaFin, String motivo, Usuario usuario) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.motivo = motivo;
        this.usuario = usuario;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}