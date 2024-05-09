package org.example.service;

import org.example.dto.HitRequest;
import org.example.dto.HitsResponse;
import org.example.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    HitsResponse create(HitRequest hitDto);

    List<StatsResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
