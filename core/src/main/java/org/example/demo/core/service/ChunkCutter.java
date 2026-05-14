package org.example.demo.core.service;

import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.dto.UploadChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for cutting large texts into smaller chunks
 */
@Slf4j
@Service
public class ChunkCutter {

    private static final int chunkSize = 2 * 1024 * 1024; // 2 MB

    public List<UploadChunk> cutIntoChunks(UUID id, String fileName, String content) {
        log.info("Cutting file {} to chunks", fileName);
        List<UploadChunk> chunks = new ArrayList<>();
        int start = 0;

        while (start < content.length()) {
            int min = Math.min(start + chunkSize, content.length());
            int end = min;

            if (end < content.length()) {
                while (end < content.length() && !Character.isWhitespace(content.charAt(end))) {
                    end++;
                }
                // If no whitespace found, use the chunk size anyway to keep in limits
                if (end == content.length()) {
                    end = min;
                }
            }

            String chunk = content.substring(start, end).trim();
            if (!chunk.isEmpty()) {
                chunks.add(new UploadChunk(id, fileName, chunk));
            }
            start = end;
        }

        return chunks;
    }
}
