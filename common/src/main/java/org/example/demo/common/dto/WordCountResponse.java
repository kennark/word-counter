package org.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WordCountResponse {
    private String word;
    private int count;
}
