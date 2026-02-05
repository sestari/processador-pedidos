package br.com.gerenciadorpedidos.processador.repository;

import br.com.gerenciadorpedidos.processador.entity.Pedido;
import br.com.gerenciadorpedidos.processador.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    Optional<Pedido> findByIdExterno(String idExterno);

    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);

    boolean existsByIdExterno(String idExterno);

}
