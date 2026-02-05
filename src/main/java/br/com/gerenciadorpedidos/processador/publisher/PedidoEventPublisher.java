package br.com.gerenciadorpedidos.processador.publisher;

import br.com.gerenciadorpedidos.processador.event.PedidoProcessadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.pedidos}")
    private String exchange;

    @Value("${rabbitmq.routing-key.pedido-processado}")
    private String routingKeyPedidoProcessado;

    public void publicarPedidoProcessado(PedidoProcessadoEvent event) {
        log.info("Publicando evento PedidoProcessado: idExterno={}", event.getIdExterno());
        rabbitTemplate.convertAndSend(exchange, routingKeyPedidoProcessado, event);
        log.info("Evento PedidoProcessado publicado: pedidoId={}", event.getPedidoId());
    }
}
