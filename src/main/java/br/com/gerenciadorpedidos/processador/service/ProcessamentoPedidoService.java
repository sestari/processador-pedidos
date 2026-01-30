package br.com.gerenciadorpedidos.processador.service;

import br.com.gerenciadorpedidos.processador.dto.PedidoMensagem;
import br.com.gerenciadorpedidos.processador.entity.ItemPedido;
import br.com.gerenciadorpedidos.processador.entity.Pedido;
import br.com.gerenciadorpedidos.processador.enums.StatusPedido;
import br.com.gerenciadorpedidos.processador.exception.PedidoDuplicadoException;
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

    public void processar(final PedidoMensagem mensagem) {
        try {
            final Pedido pedido = criarEValidarDuplicidadePedido(mensagem);
            processarPedido(pedido);
        } catch (PedidoDuplicadoException e) {
            log.info("Pedido duplicado: {}", mensagem.getIdExterno());
        }
    }

    public Pedido criarEValidarDuplicidadePedido(final PedidoMensagem mensagem) {

        if (existePedidoIgual(mensagem.getIdExterno())) {
            throw new PedidoDuplicadoException(mensagem.getIdExterno());
        }

        try {
            final Pedido pedido = criarPedido(mensagem);
            atualizarStatus(pedido, StatusPedido.PROCESSANDO);
            salvarPedido(pedido);
            return pedido;
        } catch (DataIntegrityViolationException e) {
            throw new PedidoDuplicadoException(mensagem.getIdExterno());
        }
    }

    public void processarPedido(final Pedido pedido) {

        log.info("Iniciando processamento do pedido: {}", pedido.getIdExterno());

        try {
            calculadoraValorPedido.calcular(pedido);
            atualizarStatus(pedido, StatusPedido.PROCESSADO);
            salvarPedido(pedido);
            log.info("Pedido processado com sucesso: {}", pedido.getIdExterno());
        } catch (Exception e) {
            log.error("Erro ao processar pedido: {}", pedido.getIdExterno(), e);
            atualizarStatus(pedido, StatusPedido.ERRO);
            pedido.setMensagemErro(e.getMessage());
            salvarPedido(pedido);
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

    private Pedido criarPedido(final PedidoMensagem mensagem) {
        final Pedido pedido = Pedido.builder()
                .idExterno(mensagem.getIdExterno())
                .status(StatusPedido.PENDENTE)
                .dataCriacao(mensagem.getDataRecebimento())
                .build();

        for (final PedidoMensagem.ItemMensagem itemMsg : mensagem.getItens()) {
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

}
