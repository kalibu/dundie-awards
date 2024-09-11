package com.ninjaone.dundie_awards.queue;

import com.google.gson.Gson;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueAwardsProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(final QueueAwardMessage queueAwardMessage) {
        final String message = new Gson().toJson(queueAwardMessage);

        rabbitTemplate.convertAndSend(this.queue.getName(), message);
    }

}
