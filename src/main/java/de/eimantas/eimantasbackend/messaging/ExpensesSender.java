package de.eimantas.eimantasbackend.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpensesSender {
  private final RabbitTemplate rabbitTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  private final Exchange exchange;

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  public ExpensesSender(RabbitTemplate rabbitTemplate, Exchange exchange) {
    this.rabbitTemplate = rabbitTemplate;
    this.exchange = exchange;

    JavaTimeModule module = new JavaTimeModule();
    mapper.registerModule(module);
  }


  public void notifyAddedExpense(JSONObject object) {
    // ... do some database stuff
    String routingKey = "expenses.added";
    logger.info("Sending to exchange: " + exchange.getName() + " with message: " + object.toString());
    rabbitTemplate.convertAndSend(exchange.getName(), routingKey, object.toString());
  }
}