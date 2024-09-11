package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Activity;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class MessageBroker {

    @Getter
    private List<Activity> messages = new LinkedList<>();

    public void sendMessage(Activity message) {
        messages.add(message);
    }
}
