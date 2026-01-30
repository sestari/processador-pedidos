package br.com.gerenciadorpedidos.processador.service;

import br.com.gerenciadorpedidos.processador.entity.ItemPedido;
import br.com.gerenciadorpedidos.processador.entity.Pedido;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculadoraValorPedido {

    public void calcular(final Pedido pedido) {
        calcularValorItens(pedido);
        calcularValorTotal(pedido);
    }

    private void calcularValorItens(final Pedido pedido) {
        for (final ItemPedido item : pedido.getItens()) {
            final BigDecimal valorTotalItem = item.getValorUnitario()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));
            item.setValorTotal(valorTotalItem);
        }
    }

    private void calcularValorTotal(final Pedido pedido) {
        final BigDecimal valorTotal = pedido.getItens().stream()
                .map(ItemPedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(valorTotal);
    }
}
