package br.com.fiap.maelink.notificacao.repository;

import br.com.fiap.maelink.notificacao.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}
