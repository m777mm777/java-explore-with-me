package org.example.adminApi.service.compilation;

import org.example.dto.request.CompilationsRequest;
import org.example.dto.response.CompilationsResponse;

public interface CompilationsService {

    CompilationsResponse crateCompilation(CompilationsRequest request);

    String removeCompilation(Long compId);

    CompilationsResponse updateCompilations(Long compId, CompilationsRequest request);
}
