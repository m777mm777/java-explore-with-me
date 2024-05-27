package org.example.adminApi.service.compilation;

import lombok.RequiredArgsConstructor;
import org.example.adminApi.repository.CompilationsRepository;
import org.example.dto.request.CompilationsRequest;
import org.example.dto.response.CompilationsResponse;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.CompilationsMapper;
import org.example.model.Compilations;
import org.example.model.Event;
import org.example.privateApi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationsMapper compilationsMapper;
    private final CompilationsRepository compilationsRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationsResponse crateCompilation(CompilationsRequest request) {
        List<Event> events = new ArrayList<>();

        if (request.getEvents() != null) {
            events = eventRepository.findAllById(request.getEvents());
        }

        if (request.getPinned() == null) {
            request.setPinned(false);
        }

        Compilations compilations = compilationsRepository.save(compilationsMapper.toCompilations(request, events));
        return compilationsMapper.toResponse(compilations);
    }

    @Override
    public String removeCompilation(Long compId) {
        compilationsRepository.findById(compId)
                .orElseThrow(() -> new ResourceNotFoundException("Compilation не найдена"));

        compilationsRepository.deleteById(compId);

        return "Подборка удалена";
    }

    @Override
    public CompilationsResponse updateCompilations(Long compId, CompilationsRequest request) {
        Compilations compilations = compilationsRepository.findById(compId)
                .orElseThrow(() -> new ResourceNotFoundException("Compilation не найдена"));

        compilations = compilationsRepository.save(toUpdateCompilations(request, compilations));

        return compilationsMapper.toResponse(compilations);
    }

    private Compilations toUpdateCompilations(CompilationsRequest request, Compilations compilations) {
        if (request.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(request.getEvents());
            compilations.setEvent(events);
        }

        if (request.getPinned() != null) {
            compilations.setPinned(request.getPinned());
        }

        if (request.getTitle() != null) {
            compilations.setTitle(request.getTitle());
        }

        return compilations;
    }
}
