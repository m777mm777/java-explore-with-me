package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CompilationsRequest;
import org.example.dto.response.CompilationsResponse;
import org.example.model.Compilations;
import org.example.model.Event;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationsMapper {

    private final EventMapper eventMapper;

    public Compilations toCompilations(CompilationsRequest request, List<Event> events) {
        if (request == null) {
            return null;
        }

        Compilations compilations = new Compilations();
        compilations.setEvent(events);
        compilations.setPinned(request.getPinned());
        compilations.setTitle(request.getTitle());

        return compilations;
    }

    public CompilationsResponse toResponse(Compilations compilations) {
        if (compilations == null) {
            return null;
        }

        CompilationsResponse.CompilationsResponseBuilder compilationsResponse = CompilationsResponse.builder();
        compilationsResponse.id(compilations.getId());
        compilationsResponse.events(eventMapper.toEventShortResponseCollection(compilations.getEvent()));
        compilationsResponse.pinned(compilations.getPinned());
        compilationsResponse.title(compilations.getTitle());

        return compilationsResponse.build();
    }

    public List<CompilationsResponse> toCompilationsResponseCollection(List<Compilations> compilations) {
        if (compilations == null) {
            return null;
        }
        List<CompilationsResponse> list = new ArrayList<CompilationsResponse>(compilations.size());

        for (Compilations compilations1 : compilations) {
            list.add(toResponse(compilations1));
        }

        return list;
    }
}
