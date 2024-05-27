package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.HitRequest;
import org.example.dto.HitsResponse;
import org.example.dto.StatsResponse;
import org.example.mapper.AllMapper;
import org.example.model.Hit;
import org.example.repository.StatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final AllMapper mapper;

    @Override
    public HitsResponse create(HitRequest hitRequest) {
        Hit hit = statsRepository.save(mapper.toHit(hitRequest));
        return mapper.toHitsResponse(hit);
    }

    @Override
    public List<StatsResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (unique) {
            return mapper.toResponseCollection(statsRepository.findUniqueViewStats(start, end, uris));
        } else {
            return mapper.toResponseCollection(statsRepository.findViewStats(start, end, uris));
        }

    }

}
