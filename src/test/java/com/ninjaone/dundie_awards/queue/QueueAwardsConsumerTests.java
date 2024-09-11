package com.ninjaone.dundie_awards.queue;

import com.google.gson.Gson;
import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class QueueAwardsConsumerTests {

    @Autowired
    private QueueAwardsConsumer consumer;

    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AwardsCache awardsCache;

    @Test
    void testAddAwardValidUser() {

        final Employee employeeTest = employeeRepository.findAll().getFirst();
        final Integer countEmployeeAwards = employeeTest.getDundieAwards() == null ? 0 : employeeTest.getDundieAwards();
        final Integer countMessageBroker = messageBroker.getMessages().size();
        final Integer countAwards = awardsCache.getTotalAwards();
        final Long countActivities = activityRepository.count();
        final QueueAwardMessage message = new QueueAwardMessage(employeeTest.getId());
        final String jsonMessage = new Gson().toJson(message);

        consumer.receive(jsonMessage);

        assertEquals(countEmployeeAwards + 1, employeeRepository.findById(employeeTest.getId()).get().getDundieAwards());
        assertEquals(countMessageBroker + 1, messageBroker.getMessages().size());
        assertEquals(countAwards + 1, awardsCache.getTotalAwards());
        assertEquals(countActivities + 1, activityRepository.count());
    }

    @Test
    void testAddAwardInvalidUser() {

        final Integer countMessageBroker = messageBroker.getMessages().size();
        final Integer countAwards = awardsCache.getTotalAwards();
        final Long countActivities = activityRepository.count();
        final QueueAwardMessage message = new QueueAwardMessage(0L);
        final String jsonMessage = new Gson().toJson(message);

        consumer.receive(jsonMessage);

        assertEquals(countMessageBroker + 1, messageBroker.getMessages().size());
        assertEquals(countAwards, awardsCache.getTotalAwards());
        assertEquals(countActivities, activityRepository.count());
    }

    @Test
    void testAddAwardNullUser() {

        final Integer countMessageBroker = messageBroker.getMessages().size();
        final Integer countAwards = awardsCache.getTotalAwards();
        final Long countActivities = activityRepository.count();
        final QueueAwardMessage message = new QueueAwardMessage();
        final String jsonMessage = new Gson().toJson(message);

        consumer.receive(jsonMessage);

        assertEquals(countMessageBroker + 1, messageBroker.getMessages().size());
        assertEquals(countAwards, awardsCache.getTotalAwards());
        assertEquals(countActivities, activityRepository.count());
    }
}
