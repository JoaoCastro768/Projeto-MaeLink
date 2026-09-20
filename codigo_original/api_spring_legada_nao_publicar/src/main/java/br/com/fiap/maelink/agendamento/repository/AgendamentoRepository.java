package br.com.fiap.maelink.agendamento.repository;

import br.com.fiap.maelink.agendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByDoadoraIdOrderByDataDesc(Long doadoraId);
}
