package co.salonglitt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recordatorios")
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrecordatorios")
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String mensaje;

    @Column(name = "fecha_recordatorio", nullable = false, length = 100)
    private String fechaRecordatorio;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    public Recordatorio() {
    }

    public Recordatorio(String titulo, String mensaje, String fechaRecordatorio, Usuario usuario) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaRecordatorio = fechaRecordatorio;
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdrecordatorios() {
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaRecordatorio() {
        return fechaRecordatorio;
    }

    public void setFechaRecordatorio(String fechaRecordatorio) {
        this.fechaRecordatorio = fechaRecordatorio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}