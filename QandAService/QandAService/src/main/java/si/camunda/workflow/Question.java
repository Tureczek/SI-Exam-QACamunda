package si.camunda.workflow;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.camunda.workflow.Consumer.ConsumerService;
import si.camunda.workflow.Producer.ProducerService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Named
public class Question implements JavaDelegate {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ProducerService producerService;

    @Autowired
    ConsumerService consumerService;

    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    List<String> similarQuestionList = new ArrayList<>();

    public void execute(DelegateExecution execution) throws Exception {

        String question = (String) execution.getVariable("question");
        String bk = execution.getBusinessKey();
        logger.info("Message is : " + question + " With Business Key : " + bk);

        producerService.createQueue(question);

        TimeUnit.SECONDS.sleep(5);

        similarQuestionList.add(consumerService.fetchMessage());
        logger.info(similarQuestionList.toString());
        logger.info("Passed 5 second sleeper, with message : " + consumerService.fetchMessage());


        execution.setVariable("answer", consumerService.fetchMessage());
    }
/*

    @KafkaListener(topics = "similar-questions", groupId = "questionable-group")
    public void getSimilarQuestions(String answers){


        System.out.println("Consumed message : " + answers);
        logger.info("&&& Message [{}] consumed", answers);

        similarQuestionList.add(answers);
        System.out.println(similarQuestionList);

        for (int i = 0; i < similarQuestionList.size(); i++) {
            ProcessInstance startProcess = runtimeService. createMessageCorrelation("externalAnswer")
                    //.processInstanceBusinessKey("1")
                    .setVariable("question", similarQuestionList.get(i))
                    .correlateStartMessage();
        }
    }

 */
}
