package org.example.mapper;

import org.example.dto.StatsResponse;
import org.example.model.Hit;
import org.example.dto.HitRequest;
import org.example.dto.HitsResponse;
import org.example.dto.StatsRequest;
import org.example.model.Stats;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AllMapper {

    public Hit toHit(HitRequest hitRequest) {
        if (hitRequest == null) {
            return null;
        }

        Hit hit = new Hit();
        hit.setApp(hitRequest.getApp());
        hit.setUri(hitRequest.getUri());
        hit.setIp(hitRequest.getIp());
        hit.setTimestamp(hitRequest.getTimestamp());
        return hit;
    }

    public HitsResponse toHitsResponse(Hit hits) {
        if (hits == null) {
            return null;
        }

        HitsResponse hitsResponse = new HitsResponse();
        hitsResponse.setApp(hits.getApp());
        hitsResponse.setUri(hits.getUri());
        hitsResponse.setIp(hits.getIp());
        hitsResponse.setTimestamp(hits.getTimestamp());
        return hitsResponse;
    }

    Stats toStats(StatsRequest statsRequest) {
        if (statsRequest == null) {
            return null;
        }

        Stats stats = new Stats();
        stats.setApp(statsRequest.getApp());
        stats.setUri(statsRequest.getUri());
        stats.setHits(statsRequest.getHits());
        return stats;
    }

    StatsResponse toStatsResponse(Stats stats) {
        if (stats == null) {
            return null;
        }

        StatsResponse statsResponse = new StatsResponse();
        statsResponse.setApp(stats.getApp());
        statsResponse.setUri(stats.getUri());
        statsResponse.setHits(stats.getHits());
        return statsResponse;
    }

    public List<StatsResponse> toResponseCollection(List<Stats> stats) {
        if (stats.isEmpty()) {
            List<StatsResponse> list = new ArrayList<>();
            return list;
        }

        List<StatsResponse> list = new ArrayList<StatsResponse>(stats.size());

        for (Stats stat : stats) {
            list.add(toStatsResponse(stat));
        }

        return list;
    }

}