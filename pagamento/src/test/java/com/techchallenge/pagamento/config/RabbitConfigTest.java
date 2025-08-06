package com.techchallenge.pagamento.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitConfigTest {

    @InjectMocks
    private RabbitConfig rabbitConfig;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Jackson2JsonMessageConverter messageConverter;

    @BeforeEach
    void setUp() {
        rabbitConfig = new RabbitConfig();
    }

    @Test
    void exchange_ShouldCreateTopicExchange() {
        Exchange exchange = rabbitConfig.exchange();

        assertNotNull(exchange);
        assertEquals(RabbitConfig.EXCHANGE, exchange.getName());
        assertEquals("topic", exchange.getType());
        assertTrue(exchange.isDurable());
    }

    @Test
    void queue_ShouldCreateDurableQueue() {
        Queue queue = rabbitConfig.queue();

        assertNotNull(queue);
        assertEquals(RabbitConfig.QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void binding_ShouldBindQueueToExchangeWithRoutingKey() {
        Queue queue = rabbitConfig.queue();
        Exchange exchange = rabbitConfig.exchange();

        Binding binding = rabbitConfig.binding(queue, exchange);

        assertNotNull(binding);
        assertEquals(RabbitConfig.ROUTING_KEY, binding.getRoutingKey());
        assertEquals(queue.getName(), binding.getDestination());
        assertEquals(exchange.getName(), binding.getExchange());
    }

    @Test
    void messageConverter_ShouldCreateJackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = rabbitConfig.messageConverter();

        assertNotNull(converter);
        assertTrue(converter instanceof Jackson2JsonMessageConverter);
    }

    @Test
    void rabbitListenerContainerFactory_ShouldConfigureFactory() {
        RabbitListenerContainerFactory<?> factory = rabbitConfig.rabbitListenerContainerFactory(
                connectionFactory, messageConverter);

        assertNotNull(factory);
        assertTrue(factory instanceof SimpleRabbitListenerContainerFactory);

        SimpleRabbitListenerContainerFactory castedFactory = (SimpleRabbitListenerContainerFactory) factory;
        assertNotNull(castedFactory);

        assertTrue(castedFactory.getClass().equals(SimpleRabbitListenerContainerFactory.class));
    }

    @Test
    void constants_ShouldHaveCorrectValues() {
        assertEquals("pedido.exchange", RabbitConfig.EXCHANGE);
        assertEquals("pedido-created-queue", RabbitConfig.QUEUE);
        assertEquals("pedido.created", RabbitConfig.ROUTING_KEY);
    }

    @Test
    void rabbitConfig_ShouldBeConfigurationClass() {
        RabbitConfig config = new RabbitConfig();

        assertNotNull(config);
        assertTrue(config.getClass().isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }
}