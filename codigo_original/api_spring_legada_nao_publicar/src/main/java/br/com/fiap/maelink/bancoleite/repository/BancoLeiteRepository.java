package br.com.fiap.maelink.bancoleite.repository;

import br.com.fiap.maelink.bancoleite.model.BancoLeite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BancoLeiteRepository extends JpaRepository<BancoLeite, Long> {
    List<BancoLeite> findByAreaAtendidaContainingIgnoreCase(String area);
}
