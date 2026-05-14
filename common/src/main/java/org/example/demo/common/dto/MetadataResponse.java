package org.example.demo.common.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
public class MetadataResponse {

    private UUID id;

    private String fileName;
    @Setter
    private ProcessType status;
    @Setter
    private List<WordCountResponse> wordCounts;

    public MetadataResponse(UUID id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }
}

