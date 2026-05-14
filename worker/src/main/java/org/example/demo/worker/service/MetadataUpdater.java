package org.example.demo.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.entity.TextUploadMetadata;
import org.example.demo.common.entity.WordCount;
import org.example.demo.common.repository.MetadataRepository;
import org.example.demo.common.repository.WordCountRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataUpdater {
    private final MetadataRepository metadataRepository;
    private final WordCountRepository wordCountRepository;

    public void updateTextFileMetadata(UUID id) {
        log.info("Updating metadata of {}", id);
        TextUploadMetadata metadata = metadataRepository.findById(id).orElse(null);
        if (metadata == null) throw new RuntimeException("metadata does not exist");

        metadata.setProcessedChunks(metadata.getProcessedChunks() + 1);

        metadataRepository.save(metadata);
    }

    private String getJsonStringFromWordCountList(List<WordCount> wordCounts) {

        List<Map<String, Object>> extract = wordCounts.stream()
                .map(c ->
                        Map.<String, Object>of(
                                "word", c.getWord(), "count", c.getCount()
                        ))
                .toList();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(extract);
    }
}
