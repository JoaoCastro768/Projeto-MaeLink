package br.com.fiap.maelink.auditoria.repository;

import br.com.fiap.maelink.auditoria.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {}
