package co.salonglitt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombreuser;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 250)
    private String passwordHash;

    @Column(name = "password_hash", length = 100)
    private String passwordHash;

    @Column(length = 30)
    private String rol;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @Column(nullable = false, length = 20)
    private String rol;

    public Usuario() {
    }

    public Usuario(String nombreuser, String email, String passwordHash, String telefono, String rol) {
        this.nombreuser = nombreuser;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.rol = rol;
    }

    public Usuario(String nombre, String email, String passwordHash, String telefono, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.rol = rol;
        this.activo = true;
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

    public void setNombreuser(String nombreuser) {
        this.nombreuser = nombreuser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}