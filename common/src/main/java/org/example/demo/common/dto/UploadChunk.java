package org.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UploadChunk {
    private UUID id;
    private String fileName;
    private String content;

}
