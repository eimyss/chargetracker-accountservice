package de.eimantas.eimantasbackend.config;

import de.eimantas.eimantasbackend.messaging.AccountsSender;
import de.eimantas.eimantasbackend.messaging.ExpensesSender;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {


  @Value("${account.messaging.exchange}")
  private String eventexchange;

  @Bean
  public Exchange eventExchange() {
    return new TopicExchange(eventexchange);
  }

  @Bean
  public AccountsSender accountsSender(RabbitTemplate rabbitTemplate, Exchange eventExchange) {
    return new AccountsSender(rabbitTemplate, eventExchange);
  }

  @Bean
  public ExpensesSender expensesSender(RabbitTemplate rabbitTemplate, Exchange eventExchange) {
    return new ExpensesSender(rabbitTemplate, eventExchange);
  }

}
