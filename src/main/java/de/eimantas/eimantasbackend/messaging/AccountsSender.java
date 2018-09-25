package de.eimantas.eimantasbackend.messaging;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountsSender {
  private final RabbitTemplate rabbitTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  private final Exchange exchange;

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  public AccountsSender(RabbitTemplate rabbitTemplate, Exchange exchange) {
    this.rabbitTemplate = rabbitTemplate;
    this.exchange = exchange;

    JavaTimeModule module = new JavaTimeModule();
    mapper.registerModule(module);
  }


  public void createExpense(AccountDTO account) throws JsonProcessingException {
    // ... do some database stuff
    String routingKey = "expenses.created";

    logger.info("Sending to exchange: " + exchange.getName() + " with message: " + account);
    rabbitTemplate.convertAndSend(exchange.getName(), routingKey, mapper.writeValueAsString(account));
  }
}