package com.techchallenge.pedido.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RabbitConfigTest {

    private RabbitConfig rabbitConfig;

    @BeforeEach
    void setUp() {
        rabbitConfig = new RabbitConfig();
    }

    @Test
    void exchange_DeveCriarTopicExchangeComNomeCorreto() {
        Exchange exchange = rabbitConfig.exchange();

        assertNotNull(exchange);
        assertEquals("pedido.exchange", exchange.getName());
        assertTrue(exchange.isDurable());
        assertEquals("topic", exchange.getType());
    }

    @Test
    void queue_DeveCriarQueueComNomeCorreto() {
        Queue queue = rabbitConfig.queue();

        assertNotNull(queue);
        assertEquals("pedido-created-queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void binding_DeveCriarBindingComRoutingKeyCorreto() {
        Queue queue = rabbitConfig.queue();
        Exchange exchange = rabbitConfig.exchange();

        Binding binding = rabbitConfig.binding(queue, exchange);

        assertNotNull(binding);
        assertEquals("pedido-created-queue", binding.getDestination());
        assertEquals("pedido.exchange", binding.getExchange());
        assertEquals("pedido.created", binding.getRoutingKey());
    }

    @Test
    void jsonMessageConverter_DeveCriarJackson2JsonMessageConverter() {
        MessageConverter converter = rabbitConfig.jsonMessageConverter();

        assertNotNull(converter);
        assertTrue(converter instanceof Jackson2JsonMessageConverter);
    }

    @Test
    void rabbitTemplate_DeveConfigurarCorretamente() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        MessageConverter messageConverter = new Jackson2JsonMessageConverter();

        RabbitTemplate rabbitTemplate = rabbitConfig.rabbitTemplate(connectionFactory, messageConverter);

        assertNotNull(rabbitTemplate);
        assertEquals(messageConverter, rabbitTemplate.getMessageConverter());
    }
}