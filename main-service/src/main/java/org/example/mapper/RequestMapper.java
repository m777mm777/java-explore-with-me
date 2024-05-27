package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.ParticipationResponse;
import org.example.model.Event;
import org.example.model.Request;
import org.example.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    public Request toRequestByEvent(Event event, User user) {
        if (event == null) {
            return null;
        }

        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        return request;
    }

    public ParticipationResponse toParticipationResponse(Request request) {
        if (request == null) {
            return null;
        }

        ParticipationResponse.ParticipationResponseBuilder participationResponse = ParticipationResponse.builder();
        participationResponse.id(request.getId());
        participationResponse.created(request.getCreated());
        participationResponse.event(request.getEvent().getId());
        participationResponse.requester(request.getRequester().getId());
        participationResponse.status(request.getStatus());
        return participationResponse.build();
    }

    public List<ParticipationResponse> toParticipationResponseCollection(List<Request> requests) {
        if (requests == null) {
            return null;
        }
        List<ParticipationResponse> list = new ArrayList<ParticipationResponse>(requests.size());

        for (Request request : requests) {
            list.add(toParticipationResponse(request));
        }

        return list;
    }
}
