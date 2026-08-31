package co.salonglitt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "galeria")
public class Galeria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idgaleria")
    private Integer id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String archivo;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @Column(nullable = false, length = 10)
    private String tipo;

    public Galeria() {
    }

    public Galeria(String titulo, String archivo, String descripcion, LocalDateTime fechaSubida, String tipo) {
        this.titulo = titulo;
        this.archivo = archivo;
        this.descripcion = descripcion;
        this.fechaSubida = fechaSubida;
        this.tipo = tipo;
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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
