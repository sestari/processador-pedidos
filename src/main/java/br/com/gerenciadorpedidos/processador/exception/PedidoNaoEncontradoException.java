package br.com.gerenciadorpedidos.processador.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(final String idExterno) {
        super("Pedido não encontrado com id externo: %s. Se você ja enviou esse pedido aguarde o mesmo ser processado em nossa fila.".formatted(idExterno));
    }

}
