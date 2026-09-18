package co.salonglitt.entity;

import jakarta.persistence.*;

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

    public Perfil() {
    }

    public Perfil(String nombre, String apellido, String bio, Usuario usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.bio = bio;
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}