package org.example.demo.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.dto.UploadChunk;
import org.example.demo.common.entity.WordCount;
import org.example.demo.common.repository.WordCountRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextProcessor {

    private final WordCountRepository wordCountRepository;

    // Some common words, that don't have much meaning
    private final HashSet<String> excludedWords = new HashSet<>() {{
        add("a");
        add("an");
        add("the");
        add("and");
        add("or");
        add("but");
        add("is");
        add("are");
        add("was");
        add("were");
        add("be");
        add("have");
        add("has");
        add("had");
    }};

    public void processText(UploadChunk chunk) {
        List<WordCount> existingCounts = getWordCountsByTextId(chunk.getId());

        HashMap<String, Integer> wordCount = processWordCount(chunk);

        combineWordCountList(wordCount, chunk.getId(), existingCounts);

        log.info("Saving current word counts");
        wordCountRepository.saveAllAndFlush(existingCounts);

    }

    private HashMap<String, Integer> processWordCount(UploadChunk chunk) {
        HashMap<String, Integer> wordCount;

        log.info("Processing word count");

        Stream<String> words = Stream.of(chunk.getContent().split("\\W+"));

        // Count all words and increment the counts in wordCount
        wordCount = words.collect(HashMap::new, (map, word) -> {
            if (!excludedWords.contains(word.toLowerCase()) && !word.isEmpty()) {
                map.put(word, map.getOrDefault(word, 0) + 1);
            }
        }, HashMap::putAll);

        return wordCount;
    }

    private void combineWordCountList(HashMap<String, Integer> wordCountMap, UUID textId, List<WordCount> existingCounts) {
        log.info("Combining word counts");

        wordCountMap.forEach((word, count) -> {
            WordCount existingCount = existingCounts.stream()
                    .filter(wc -> wc.getWord().equals(word))
                    .findFirst()
                    .orElse(null);

            if (existingCount != null) {
                existingCount.setCount(existingCount.getCount() + count);
            } else {
                WordCount newWordCount = new WordCount(textId, word);
                newWordCount.setCount(count);
                existingCounts.add(newWordCount);
            }
        });
    }


    // Find all WordCounts from WordCountRepository by the textId field
    private List<WordCount> getWordCountsByTextId(UUID textId) {
        log.info("Getting word counts of text id: {}", textId);
        return wordCountRepository.findAllByTextIdEquals(textId);
    }

}
