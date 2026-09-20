package br.com.fiap.maelink.doacao.repository;

import br.com.fiap.maelink.doacao.model.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
    Optional<Doacao> findByAgendamentoId(Long agendamentoId);
}
