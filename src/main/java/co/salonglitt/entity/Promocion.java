package co.salonglitt.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promociones")
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpromocion")
    private Integer id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column
    private Boolean activa;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Promocion() {
    }

    public Promocion(String titulo, String descripcion, Boolean activa) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.activa = activa;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}