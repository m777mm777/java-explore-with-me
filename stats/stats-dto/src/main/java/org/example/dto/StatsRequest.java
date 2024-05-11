package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatsRequest {

    private String app;

    private String uri;

    private Long hits;
}
