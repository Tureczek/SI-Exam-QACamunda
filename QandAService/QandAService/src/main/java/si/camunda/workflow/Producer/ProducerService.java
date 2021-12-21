package si.camunda.workflow.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;



@Service
public class ProducerService {

    private static Logger logger = LoggerFactory.getLogger(ProducerService.class);

    private static String queueName = null; // never used here
    private final static String EXCHANGE_NAME = "direct-exchange-camunda";
    private static String routingKey = "question";


    public void createQueue(String message) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        logger.info("This is the message : " + message + " - Now sending through RabbitMQ Producer with exchange name : " + EXCHANGE_NAME);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel())
        {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
        }

    }

}
