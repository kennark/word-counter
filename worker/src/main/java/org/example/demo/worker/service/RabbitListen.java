package org.example.demo.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.dto.UploadChunk;
import org.example.demo.worker.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitListen {

    private final TextProcessor textProcessor;
    private final MetadataUpdater metadataUpdater;
    
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(UploadChunk chunk) {
        log.info("Received chunk: {}", chunk.getId());
        textProcessor.processText(chunk);
        metadataUpdater.updateTextFileMetadata(chunk.getId());
    }
}
