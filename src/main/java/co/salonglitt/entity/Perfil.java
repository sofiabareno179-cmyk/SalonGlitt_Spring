package co.salonglitt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "perfiles")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    @Column(columnDefinition = "text")
    private String bio;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false, unique = true)
    private Usuario usuario;

    @Transient
    private String apellido;

    @Transient
    private String bio;

    @Transient
    private Usuario usuario;

    public Perfil() {
    }

    public Perfil(String nombre, String apellido, String bio, Usuario usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.bio = bio;
        this.usuario = usuario;
    }

    public Perfil(String nombre, String apellido, String bio, Usuario usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.bio = bio;
        this.descripcion = bio;
        this.usuario = usuario;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getBio() { return bio == null ? descripcion : bio; }
    public void setBio(String bio) { this.bio = bio; this.descripcion = bio; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
