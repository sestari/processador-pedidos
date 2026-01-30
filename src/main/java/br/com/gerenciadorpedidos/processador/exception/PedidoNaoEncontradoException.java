package br.com.gerenciadorpedidos.processador.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(String idExterno) {
        super("Pedido não encontrado com idExterno: " + idExterno);
    }

    public PedidoNaoEncontradoException(Long id) {
        super("Pedido não encontrado com id: " + id);
    }
}
