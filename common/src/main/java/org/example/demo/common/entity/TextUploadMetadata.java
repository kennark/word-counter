package org.example.demo.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@Table(name = "uploads")
@NoArgsConstructor
public class TextUploadMetadata {

    public TextUploadMetadata(UUID id, String fileName, int chunkCount) {
        this.id = id;
        this.chunkCount = chunkCount;
        this.fileName = fileName;
        this.processedChunks = 0;
    }

    @Id
    private UUID id;

    private String fileName;
    private int chunkCount;
    private int processedChunks;

}
