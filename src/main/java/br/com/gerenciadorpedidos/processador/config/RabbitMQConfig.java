package br.com.gerenciadorpedidos.processador.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.pedidos}")
    private String pedidosExchange;

    @Value("${rabbitmq.routing-key.pedido-recebido}")
    private String routingKeyPedidoRecebido;

    @Value("${rabbitmq.queue.processamento}")
    private String filaProcessamento;

    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(pedidosExchange, true, false);
    }

    @Bean
    public Queue filaProcessamento() {
        return QueueBuilder.durable(filaProcessamento).build();
    }

    @Bean
    public Binding bindingProcessamento() {
        return BindingBuilder
                .bind(filaProcessamento())
                .to(pedidosExchange())
                .with(routingKeyPedidoRecebido);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {

        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(1);
        return factory;
    }
}
