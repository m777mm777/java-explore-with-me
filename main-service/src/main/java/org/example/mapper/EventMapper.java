package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.EventRequest;
import org.example.dto.request.UpdateEventAdminRequest;
import org.example.dto.request.UpdateEventUserRequest;
import org.example.dto.response.EventResponse;
import org.example.dto.response.EventShortResponse;
import org.example.enums.State;
import org.example.enums.StateAction;
import org.example.enums.StateActionAdmin;
import org.example.model.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public Event toEvent(EventRequest request) {
        if (request == null) {
            return null;
        }

        Event event = new Event();
        event.setAnnotation((request.getAnnotation()));
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setLocation(request.getLocation());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setRequestModeration(request.getRequestModeration());
        event.setTitle(request.getTitle());

        return event;
    }

    public Event toEventByUpdateUserRequest(UpdateEventUserRequest request, Event event) {
        if (request == null) {
            return null;
        }

        if (request.getAnnotation() != null && !request.getAnnotation().isEmpty()) {
            event.setAnnotation((request.getAnnotation()));
        }
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null && !request.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null && request.getParticipantLimit() >= 0) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else if (request.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        return event;
    }

    public Event toEventByUpdateAdminRequest(UpdateEventAdminRequest request, Event event) {
        if (request == null) {
            return null;
        }
        if (request.getAnnotation() != null && !request.getAnnotation().isEmpty()) {
            event.setAnnotation((request.getAnnotation()));
        }

        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null && !request.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null && request.getParticipantLimit() >= 0) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            } else if (request.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        return event;
    }

    public EventResponse toResponse(Event event) {
        if (event == null) {
            return null;
        }

        EventResponse.EventResponseBuilder eventResponse = EventResponse.builder();
        eventResponse.id(event.getId());
        eventResponse.annotation(event.getAnnotation());
        eventResponse.category(event.getCategory());
        eventResponse.confirmedRequests(event.getConfirmedRequests());
        eventResponse.createdOn(event.getCreatedOn());
        eventResponse.description(event.getDescription());
        eventResponse.eventDate(event.getEventDate());
        eventResponse.initiator(event.getInitiator());
        eventResponse.location(event.getLocation());
        eventResponse.paid(event.getPaid());
        eventResponse.participantLimit(event.getParticipantLimit());
        eventResponse.publishedOn(event.getPublishedOn());
        eventResponse.requestModeration(event.getRequestModeration());
        eventResponse.state(event.getState());
        eventResponse.title(event.getTitle());
        eventResponse.views(event.getViews());
        return eventResponse.build();
    }

    public EventShortResponse toEventShortResponse(Event event) {
        if (event == null) {
            return null;
        }

        EventShortResponse.EventShortResponseBuilder eventShortResponse = EventShortResponse.builder();
        eventShortResponse.id(event.getId());
        eventShortResponse.annotation(event.getAnnotation());
        eventShortResponse.category(categoryMapper.toResponse(event.getCategory()));
        eventShortResponse.confirmedRequests(event.getConfirmedRequests());
        eventShortResponse.eventDate(event.getEventDate());
        eventShortResponse.initiator(userMapper.toResponse(event.getInitiator()));
        eventShortResponse.paid(event.getPaid());
        eventShortResponse.title(event.getTitle());
        eventShortResponse.views(event.getViews());
        return eventShortResponse.build();
    }

    public List<EventResponse> toResponseCollection(List<Event> events) {
        if (events == null) {
            return null;
        }
        List<EventResponse> list = new ArrayList<EventResponse>(events.size());

        for (Event event : events) {
            list.add(toResponse(event));
        }

        return list;
    }

    public List<EventShortResponse> toEventShortResponseCollection(List<Event> events) {
        if (events == null) {
            return null;
        }
        List<EventShortResponse> list = new ArrayList<EventShortResponse>(events.size());

        for (Event event : events) {
            list.add(toEventShortResponse(event));
        }

        return list;
    }
}
