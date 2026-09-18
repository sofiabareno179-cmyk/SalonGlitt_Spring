package co.salonglitt.entity;

import jakarta.persistence.*;

@Entity
@IdClass(ProductoProveedorId.class)
@Table(name = "producto_proveedores")
public class ProductoProveedor {

    @Id
    @Column(name = "producto_id")
    private Integer productoId;

    @Id
    @Column(name = "proveedor_id")
    private Integer proveedorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id", insertable = false, updatable = false)
    private Proveedor proveedor;

    public ProductoProveedor() {
    }

    public ProductoProveedor(Integer productoId, Integer proveedorId) {
        this.productoId = productoId;
        this.proveedorId = proveedorId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}
