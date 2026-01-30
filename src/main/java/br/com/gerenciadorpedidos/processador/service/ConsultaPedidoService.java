package br.com.gerenciadorpedidos.processador.service;

import br.com.gerenciadorpedidos.processador.dto.PedidoResponse;
import br.com.gerenciadorpedidos.processador.entity.Pedido;
import br.com.gerenciadorpedidos.processador.enums.StatusPedido;
import br.com.gerenciadorpedidos.processador.exception.PedidoNaoEncontradoException;
import br.com.gerenciadorpedidos.processador.mapper.PedidoMapper;
import br.com.gerenciadorpedidos.processador.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultaPedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoResponse buscarPorIdExterno(final String idExterno) {
        Pedido pedido = pedidoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new PedidoNaoEncontradoException(idExterno));
        return pedidoMapper.toResponse(pedido);
    }

    public Page<PedidoResponse> listarTodos(final Pageable pageable) {
        return pedidoRepository.findAll(pageable)
                .map(pedidoMapper::toResponse);
    }

    public Page<PedidoResponse> listarPorStatus(final StatusPedido status, final Pageable pageable) {
        return pedidoRepository.findByStatus(status, pageable)
                .map(pedidoMapper::toResponse);
    }
}
