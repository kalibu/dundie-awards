package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MessageBrokerTests {

    @Autowired
    private MessageBroker messageBroker;

    @Test
    public void testSendMessage() {
        final int countMessages = messageBroker.getMessages().size();

        messageBroker.sendMessage(new Activity());

        assertEquals(countMessages + 1, messageBroker.getMessages().size());
    }

}
