package br.com.fiap.maelink.triagem.repository;

import br.com.fiap.maelink.triagem.model.Triagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TriagemRepository extends JpaRepository<Triagem, Long> {
    List<Triagem> findByDoadoraIdOrderByCriadoEmDesc(Long doadoraId);
    Optional<Triagem> findFirstByDoadoraIdOrderByCriadoEmDesc(Long doadoraId);
}
