package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.queue.QueueAwardMessage;
import com.ninjaone.dundie_awards.queue.QueueAwardsProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/queueAward")
@Tag(name = "queueAward", description = "Rest API for QueueAward")
@Log
public class QueueAwardController {

    @Autowired
    private QueueAwardsProducer queueSender;

    @Operation(
            operationId = "/",
            summary = "Send new message",
            description = "Send new message",
            tags = {"queueAward"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = QueueAwardMessage.class))
                    })
            }
    )
    @PostMapping
    public QueueAwardMessage send(@RequestBody QueueAwardMessage message){
        log.info("SendMessage=" + message);
        queueSender.send(message);
        return message;
    }

}
