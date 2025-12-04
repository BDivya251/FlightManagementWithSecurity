package com.book.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
//import org.springframework.amqp.support.converter.JsonMessageConverter;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	public static final String EMAIL_QUEUE = "email-queue";
	public static final String EMAIL_EXCHANGE = "email-exchange";
	public static final String EMAIL_ROUTING_KEY = "email.routing.key";

	@Bean
	public Queue emailQueue() {
		return new Queue(EMAIL_QUEUE, true);
	}

	@Bean
	public TopicExchange emailExchange() {
		return new TopicExchange(EMAIL_EXCHANGE);
	}

	@Bean
	public Binding emailBinding() {
		return BindingBuilder.bind(emailQueue()).to(emailExchange()).with(EMAIL_ROUTING_KEY);
	}

	@Bean
	public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
		return new JacksonJsonMessageConverter();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
			JacksonJsonMessageConverter converter) {

		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(converter);
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter converter) {

		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(converter);
		return template;
	}

}
