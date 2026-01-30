package br.com.gerenciadorpedidos.processador.mapper;

import br.com.gerenciadorpedidos.processador.dto.ItemPedidoResponse;
import br.com.gerenciadorpedidos.processador.dto.PedidoResponse;
import br.com.gerenciadorpedidos.processador.entity.ItemPedido;
import br.com.gerenciadorpedidos.processador.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .idExterno(pedido.getIdExterno())
                .status(pedido.getStatus())
                .valorTotal(pedido.getValorTotal())
                .dataCriacao(pedido.getDataCriacao())
                .dataAtualizacao(pedido.getDataAtualizacao())
                .mensagemErro(pedido.getMensagemErro())
                .itens(toItemResponseList(pedido.getItens()))
                .build();
    }

    private List<ItemPedidoResponse> toItemResponseList(List<ItemPedido> itens) {
        return itens.stream()
                .map(this::toItemResponse)
                .toList();
    }

    private ItemPedidoResponse toItemResponse(ItemPedido item) {
        return ItemPedidoResponse.builder()
                .id(item.getId())
                .idProduto(item.getIdProduto())
                .nomeProduto(item.getNomeProduto())
                .quantidade(item.getQuantidade())
                .valorUnitario(item.getValorUnitario())
                .valorTotal(item.getValorTotal())
                .build();
    }
}
