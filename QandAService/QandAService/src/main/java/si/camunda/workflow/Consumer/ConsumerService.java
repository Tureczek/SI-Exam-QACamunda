package si.camunda.workflow.Consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumerService {

    //Consumes messages from QACategorization


    private static final String EXCHANGE_NAME = "direct-answer-exchange";
    private static String bindingKey = "answer";

    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Bean
    public static void connectQueue() throws Exception
    {
        // Same as the producer: tries to create a queue, if it wasn't already created
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Register for an exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // Get the automatically generated qname for this exchange
        String queueName = channel.queueDeclare().getQueue();
        // Bind the exchange to the queue
        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        //channel.queueBind(queueName, EXCHANGE_NAME, "");

        // Get notified, if a message for this receiver arrives
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [3] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" +  message + "'");


        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
