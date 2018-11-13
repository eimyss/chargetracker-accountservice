package de.eimantas.eimantasbackend.messaging;

import de.eimantas.eimantasbackend.service.AccountService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;


public class TransactionReceiver {

  private static final Logger logger = LoggerFactory.getLogger(TransactionReceiver.class);

  @Inject
  private AccountService service;


  public TransactionReceiver() {
  }



  //  @RabbitListener(queues = "orderServiceQueue")
  public void receive(String message) {
    logger.info("Received message '{}'", message);
  }

  public void handleMessage(Object message) throws IOException {
    logger.info("booking processed notification message '{}'", message);

    JSONObject json = null;
    try {
      json = new JSONObject((String) message);
      String type = getType(json);
      if (type.equalsIgnoreCase("expense")) {
        service.processTransaction(json);
      } else {
        logger.warn("Unknown type " + type);
      }
    } catch (JSONException e) {
      logger.warn("failed to parse body : " + message);
      e.printStackTrace();
    }


  }

  private String getType(JSONObject json) {

    try {
      return json.getString("type");
    } catch (JSONException e) {
      logger.error("cannot determine transaction type", e);
      e.printStackTrace();
    }

    return "unknown";
  }


}
