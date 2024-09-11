package com.ninjaone.dundie_awards.queue;

import com.google.gson.Gson;
import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Log
public class QueueAwardsConsumer {

    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AwardsCache awardsCache;

    @Autowired
    private ActivityRepository activityRepository;

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String message) {
        log.info("Message=" + message);

        final Activity activity = new Activity(LocalDateTime.now(), message);
        messageBroker.sendMessage(activity);

        try {
            final QueueAwardMessage awardMessage = new Gson().fromJson(message, QueueAwardMessage.class);
            Optional<Employee> employee = employeeRepository.findById(awardMessage.getEmployeeId());
            if (employee.isPresent()) {
                final Employee e = employee.get();
                e.setDundieAwards(e.getDundieAwards() == null ? 1 : (e.getDundieAwards() + 1));
                employeeRepository.save(e);

                activityRepository.save(activity);
                awardsCache.addOneAward();
            } else {
                log.severe("User not found, id=" + awardMessage.getEmployeeId());
            }
        }catch (Exception e){
            log.severe(e.getMessage());
        }
    }
}