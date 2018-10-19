package de.eimantas.eimantasbackend.config;

import de.eimantas.eimantasbackend.messaging.TransactionReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {


  @Bean
  public TopicExchange eventExchange() {
    return new TopicExchange("eventExchange");
  }

  @Bean
  public Queue bookingQueue() {
    return new Queue("transactionServiceQueue");
  }


  @Bean
  Binding bookingsBinding() {
    return BindingBuilder.bind(bookingQueue()).to(eventExchange()).with("transaction.processed");
  }

  @Bean
  public TransactionReceiver transactionReceiver() {
    return new TransactionReceiver();
  }


  @Bean
  public MessageListenerAdapter transactionListenerAdapter(TransactionReceiver receiver) {
    return new MessageListenerAdapter(receiver);
  }


  @Bean
  SimpleMessageListenerContainer bookingContainer(ConnectionFactory connectionFactory,
                                                  @Qualifier("transactionListenerAdapter") MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames("transactionServiceQueue");
    container.setMessageListener(listenerAdapter);
    return container;
  }


}
