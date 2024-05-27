package org.example.publicApi.service.compilations;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.CompilationsResponse;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.CompilationsMapper;
import org.example.model.Compilations;
import org.example.publicApi.repository.CompilationsRepositoryPublic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationsServicePublicImpl implements CompilationsServicePublic {

    private final CompilationsRepositoryPublic compilationsRepositoryPublic;
    private final CompilationsMapper compilationsMapper;
    private static final Sort SORT_ID_ASC = Sort.by(Sort.Direction.ASC, "id");

    @Override
    public List<CompilationsResponse> getAllCompilations(Boolean pinned, Integer from, Integer size) {

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_ID_ASC);

        Page<Compilations> compilationsPage;

        if (pinned == null) {
            compilationsPage = compilationsRepositoryPublic.findAll(page);
        } else {
            compilationsPage = compilationsRepositoryPublic.findByPinned(pinned, page);
        }

        return compilationsMapper.toCompilationsResponseCollection(compilationsPage.getContent());
    }

    @Override
    public CompilationsResponse getCompilations(Long compId) {
        Compilations compilations = compilationsRepositoryPublic.findById(compId)
                .orElseThrow(() -> new ResourceNotFoundException("Compilations не найдена"));

       return compilationsMapper.toResponse(compilations);
    }
}
