package co.salonglitt.entity;

import jakarta.persistence.*;

@Entity
@IdClass(ServicioProductoId.class)
@Table(name = "servicio_productos")
public class ServicioProducto {

    @Id
    @Column(name = "servicio_id")
    private Integer servicioId;

    @Id
    @Column(name = "producto_id")
    private Integer productoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio_id", insertable = false, updatable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;

    public ServicioProducto() {
    }

    public ServicioProducto(Integer servicioId, Integer productoId) {
        this.servicioId = servicioId;
        this.productoId = productoId;
    }

    public Integer getServicioId() {
        return servicioId;
    }

    public void setServicioId(Integer servicioId) {
        this.servicioId = servicioId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
