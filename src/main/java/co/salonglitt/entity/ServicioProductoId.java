package co.salonglitt.entity;

import java.io.Serializable;
import java.util.Objects;

public class ServicioProductoId implements Serializable {

    private Integer servicioId;
    private Integer productoId;

    public ServicioProductoId() {
    }

    public ServicioProductoId(Integer servicioId, Integer productoId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicioProductoId)) return false;
        ServicioProductoId that = (ServicioProductoId) o;
        return Objects.equals(servicioId, that.servicioId)
                && Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(servicioId, productoId);
    }
}
