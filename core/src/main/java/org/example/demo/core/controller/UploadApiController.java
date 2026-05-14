package org.example.demo.core.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.dto.MetadataResponse;
import org.example.demo.common.dto.UploadResponse;
import org.example.demo.core.service.ChunkCutter;
import org.example.demo.core.service.MetadataService;
import org.example.demo.core.service.RabbitPublisher;
import org.example.demo.common.dto.UploadChunk;
import org.example.demo.common.entity.TextUploadMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin
public class UploadApiController {

    private final RabbitPublisher rabbitPublisher;
    private final ChunkCutter chunkCutter;
    private final MetadataService metadataService;


    @PostMapping("upload")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws URISyntaxException, IOException {
        UUID id = UUID.randomUUID();

        String fileName = file.getOriginalFilename();
        String content = new String(file.getBytes(), Charset.defaultCharset());

        log.info("Received file: {}, size: {} bytes", fileName, content.length());

        List<UploadChunk> chunkList = chunkCutter.cutIntoChunks(id, fileName, content);
        metadataService.createMetadata(new TextUploadMetadata(id, fileName, chunkList.size()));
        rabbitPublisher.publish(chunkList);

        return ResponseEntity.created(new URI(id.toString())).body(new UploadResponse(id));
    }


    @GetMapping("upload/{id}")
    public ResponseEntity<MetadataResponse> upload(@PathVariable UUID id, @RequestParam int minCount) {
        log.info("Retrieving file id: {}", id);
        MetadataResponse data = metadataService.buildMetadataResponse(id, minCount);
        if (data == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data);
    }

}
