package co.salonglitt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinventario")
    private Integer id;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 100)
    private String fecha;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idproductos", nullable = false, unique = true)
    private Producto producto;

    @Column(length = 20)
    private String tipo;

    public Inventario() {
    }

    public Inventario(Integer stock, String fecha, Producto producto, String tipo) {
        this.stock = stock;
        this.fecha = fecha;
        this.producto = producto;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}