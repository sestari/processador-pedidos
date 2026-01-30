package br.com.gerenciadorpedidos.processador.exception;

public class PedidoDuplicadoException extends RuntimeException {

    public PedidoDuplicadoException(String idExterno) {
        super("Pedido já existe com idExterno: " + idExterno);
    }
}
