package co.salonglitt.repository;

import co.salonglitt.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import co.salonglitt.entity.Usuario;


import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    List<Agenda> findByUsuarioId(Integer usuarioId);

    boolean existsByUsuario(Usuario usuario);
}