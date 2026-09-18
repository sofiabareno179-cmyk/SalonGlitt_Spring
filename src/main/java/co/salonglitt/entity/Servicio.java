package co.salonglitt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idservicio")
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false, length = 50)
    private String duracion;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(length = 500)
    private String imagen;

    @Column(name = "idcitas")
    private Integer idcitas;

    public Servicio() {
    }

    public Servicio(String nombre, BigDecimal precio, String duracion, String categoria, String imagen, Integer idcitas) {
        this.nombre = nombre;
        this.precio = precio;
        this.duracion = duracion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.idcitas = idcitas;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getIdcitas() {
        return idcitas;
    }

    public void setIdcitas(Integer idcitas) {
        this.idcitas = idcitas;
    }
}