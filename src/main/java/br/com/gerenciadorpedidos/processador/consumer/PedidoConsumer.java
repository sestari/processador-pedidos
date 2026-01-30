package br.com.gerenciadorpedidos.processador.consumer;

import br.com.gerenciadorpedidos.processador.dto.PedidoMensagem;
import br.com.gerenciadorpedidos.processador.exception.PedidoDuplicadoException;
import br.com.gerenciadorpedidos.processador.service.ProcessamentoPedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoConsumer {

    private final ProcessamentoPedidoService processamentoPedidoService;

    @RabbitListener(queues = "${rabbitmq.fila.pedidos}")
    public void consumir(PedidoMensagem mensagem) {
        log.info("Mensagem recebida da fila: {}", mensagem.getIdExterno());

        try {
            processamentoPedidoService.processar(mensagem);
        } catch (PedidoDuplicadoException e) {
            log.warn("Pedido duplicado ignorado: {}", mensagem.getIdExterno());
        } catch (Exception e) {
            log.error("Erro ao processar pedido: {}", mensagem.getIdExterno(), e);
            throw e;
        }
    }
}
