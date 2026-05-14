package org.example.demo.core.service;

import lombok.RequiredArgsConstructor;
import org.example.demo.common.dto.MetadataResponse;
import org.example.demo.common.dto.ProcessType;
import org.example.demo.common.dto.WordCountResponse;
import org.example.demo.common.entity.TextUploadMetadata;
import org.example.demo.common.entity.WordCount;
import org.example.demo.common.repository.MetadataRepository;
import org.example.demo.common.repository.WordCountRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetadataService {

    private final MetadataRepository metadataRepository;

    private final WordCountRepository wordCountRepository;

    public MetadataResponse buildMetadataResponse(UUID id, int minCount) {

        List<WordCount> wordCounts = wordCountRepository.findAllByTextIdEqualsAndCountGreaterThan(id, minCount);

        TextUploadMetadata metadata = metadataRepository.findById(id).orElse(null);

        MetadataResponse data = new MetadataResponse(
                metadata.getId(),
                metadata.getFileName());

        data.setStatus(metadata.getProcessedChunks() == 0 ? ProcessType.PENDING :
                metadata.getProcessedChunks() >= metadata.getChunkCount() ? ProcessType.COMPLETED
                : ProcessType.PROCESSING);


        data.setWordCounts(
                wordCounts.stream()
                        .sorted(Comparator.comparingInt(WordCount::getCount).reversed())
                        .map(c ->
                        new WordCountResponse(c.getWord(), c.getCount())).toList());

        return data;
    }

    public TextUploadMetadata createMetadata(TextUploadMetadata metadata) {
        return metadataRepository.save(metadata);
    }


}
