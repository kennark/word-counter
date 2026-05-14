package org.example.demo.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.dto.UploadChunk;
import org.example.demo.core.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(List<UploadChunk> chunkList) {
        for (UploadChunk uploadChunk : chunkList) {
            log.info("Sending chunk of {}", uploadChunk.getFileName());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, uploadChunk);
        }
    }

}
