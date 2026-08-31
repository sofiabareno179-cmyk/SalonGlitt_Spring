package co.salonglitt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idagenda")
    private Integer id;

    @Column(name = "diasemana", nullable = false)
    private String diasemana;

    @Column(name = "horainicio", nullable = false)
    private String horainicio;

    @Column(name = "horafin", nullable = false)
    private String horafin;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    public Agenda() {
    }

    public Agenda(String diasemana, String horainicio, String horafin, Usuario usuario) {
        this.diasemana = diasemana;
        this.horainicio = horainicio;
        this.horafin = horafin;
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiasemana() {
        return diasemana;
    }

    public void setDiasemana(String diasemana) {
        this.diasemana = diasemana;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public String getHorafin() {
        return horafin;
    }

    public void setHorafin(String horafin) {
        this.horafin = horafin;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
