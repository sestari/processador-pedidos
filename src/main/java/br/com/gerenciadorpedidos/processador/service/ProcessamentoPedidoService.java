package br.com.gerenciadorpedidos.processador.service;

import br.com.gerenciadorpedidos.processador.entity.ItemPedido;
import br.com.gerenciadorpedidos.processador.entity.Pedido;
import br.com.gerenciadorpedidos.processador.enums.StatusPedido;
import br.com.gerenciadorpedidos.processador.event.PedidoProcessadoEvent;
import br.com.gerenciadorpedidos.processador.event.PedidoRecebidoEvent;
import br.com.gerenciadorpedidos.processador.exception.PedidoDuplicadoException;
import br.com.gerenciadorpedidos.processador.publisher.PedidoEventPublisher;
import br.com.gerenciadorpedidos.processador.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessamentoPedidoService {

    private final PedidoRepository pedidoRepository;
    private final CalculadoraValorPedido calculadoraValorPedido;
    private final PedidoEventPublisher eventPublisher;

    public void processar(final PedidoRecebidoEvent event) {
        final Pedido pedido = inserirPedidoSeNaoExistir(event);

        if (pedido == null) {
            throw new PedidoDuplicadoException(event.getIdExterno());
        }

        processarPedido(pedido);
    }

    @Transactional
    public Pedido inserirPedidoSeNaoExistir(final PedidoRecebidoEvent event) {

        if (existePedidoIgual(event.getIdExterno())) {
            throw new PedidoDuplicadoException(event.getIdExterno());
        }

        try {
            final Pedido pedido = criarPedido(event);
            atualizarStatus(pedido, StatusPedido.PROCESSANDO);
            salvarPedido(pedido);
            return pedido;
        } catch (DataIntegrityViolationException e) {
            throw new PedidoDuplicadoException(event.getIdExterno());
        }
    }

    @Transactional(readOnly = true)
    public boolean existePedidoIgual(final String idExterno) {
        return pedidoRepository.existsByIdExterno(idExterno);
    }

    @Transactional
    public void salvarPedido(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    private void atualizarStatus(final Pedido pedido, final StatusPedido status) {
        pedido.setStatus(status);
        pedido.setDataAtualizacao(LocalDateTime.now());
    }

    private Pedido criarPedido(final PedidoRecebidoEvent mensagem) {
        final Pedido pedido = Pedido.builder()
                .idExterno(mensagem.getIdExterno())
                .status(StatusPedido.PROCESSANDO)
                .dataCriacao(mensagem.getDataRecebimento())
                .build();

        for (final PedidoRecebidoEvent.ItemEvent itemMsg : mensagem.getItens()) {
            final ItemPedido item = ItemPedido.builder()
                    .idProduto(itemMsg.getIdProduto())
                    .nomeProduto(itemMsg.getNomeProduto())
                    .quantidade(itemMsg.getQuantidade())
                    .valorUnitario(itemMsg.getValorUnitario())
                    .build();
            pedido.adicionarItem(item);
        }

        return pedido;
    }


    public void processarPedido(final Pedido pedido) {
        log.info("Iniciando processamento do pedido: {}", pedido.getIdExterno());

        try {
            calculadoraValorPedido.calcular(pedido);
            pedido.setStatus(StatusPedido.PROCESSADO);
            pedido.setDataAtualizacao(LocalDateTime.now());
            pedidoRepository.save(pedido);

            log.info("Pedido processado com sucesso: {}", pedido.getIdExterno());

            eventPublisher.publicarPedidoProcessado(PedidoProcessadoEvent.builder()
                    .pedidoId(pedido.getId())
                    .idExterno(pedido.getIdExterno())
                    .valorTotal(pedido.getValorTotal())
                    .dataProcessamento(pedido.getDataAtualizacao())
                    .build());

        } catch (Exception e) {
            log.error("Erro ao processar pedido: {}", pedido.getIdExterno(), e);
            pedido.setStatus(StatusPedido.ERRO);
            pedido.setMensagemErro(e.getMessage());
            pedido.setDataAtualizacao(LocalDateTime.now());
            pedidoRepository.save(pedido);
        }
    }

}
