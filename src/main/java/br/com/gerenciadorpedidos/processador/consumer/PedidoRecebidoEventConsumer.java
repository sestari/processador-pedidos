package br.com.gerenciadorpedidos.processador.consumer;

import br.com.gerenciadorpedidos.processador.event.PedidoRecebidoEvent;
import br.com.gerenciadorpedidos.processador.service.ProcessamentoPedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoRecebidoEventConsumer {

    private final ProcessamentoPedidoService processamentoPedidoService;

    @RabbitListener(queues = "${rabbitmq.queue.processamento}")
    public void onPedidoRecebido(PedidoRecebidoEvent event) {
        log.info("Evento recebido: idExterno={}, eventId={}", event.getIdExterno(), event.getIdExterno());

        try {
            processamentoPedidoService.processar(event);
        } catch (Exception e) {
            log.error("Erro ao processar evento: idExterno={}", event.getIdExterno(), e);
            throw e;
        }
    }
}
