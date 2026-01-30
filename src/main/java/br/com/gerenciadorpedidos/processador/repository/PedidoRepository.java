package br.com.gerenciadorpedidos.processador.repository;

import br.com.gerenciadorpedidos.processador.entity.Pedido;
import br.com.gerenciadorpedidos.processador.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    boolean existsByIdExterno(String idExterno);

    Optional<Pedido> findByIdExterno(String idExterno);

    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);
}
