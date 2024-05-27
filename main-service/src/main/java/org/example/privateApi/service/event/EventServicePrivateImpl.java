package org.example.privateApi.service.event;

import lombok.RequiredArgsConstructor;
import org.example.adminApi.repository.CategoryRepository;
import org.example.dto.request.EventRequestStatusUpdateRequest;
import org.example.dto.request.UpdateEventUserRequest;
import org.example.dto.response.EventRequestStatusUpdateResult;
import org.example.enums.Status;
import org.example.exceptions.ConflictServerError;
import org.example.exceptions.ValidationException;
import org.example.mapper.RequestMapper;
import org.example.model.Request;
import org.example.privateApi.repository.EventRepository;
import org.example.adminApi.repository.UserRepository;
import org.example.dto.request.EventRequest;
import org.example.dto.response.EventResponse;
import org.example.dto.response.EventShortResponse;
import org.example.dto.response.ParticipationResponse;
import org.example.enums.State;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.EventMapper;
import org.example.model.Category;
import org.example.model.Event;
import org.example.model.User;
import org.example.privateApi.repository.RequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServicePrivateImpl implements EventService {

    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;
    private static final Sort SORT_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventResponse create(Long userId, EventRequest request) {

        if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Начало Event должно быть не раньше чем"
                    + LocalDateTime.now().plusHours(2).format(FORMAT));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена"));

        if (request.getPaid() == null) {
            request.setPaid(false);
        }

        if (request.getRequestModeration() == null) {
            request.setRequestModeration(true);
        }

        if (request.getParticipantLimit() == null) {
            request.setParticipantLimit(0);
        }

        Event event = eventMapper.toEvent(request);
        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0);
        event.setViews(0L);
        event.setState(State.PENDING);
        event = eventRepository.save(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse updateOwnerEvent(Long userId, Long eventId, UpdateEventUserRequest request) {

        if (request.getEventDate() != null) {
            if (!request.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Дата начала должна быть не раньше чем "
                        + LocalDateTime.now().plusHours(2).format(FORMAT));
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictServerError("Редактировать опубликованные события нельзя");
        }

        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictServerError("Редактировать может только создатель");
        }

        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена"));

            event.setCategory(category);
        }

        Event eventUpdate = eventMapper.toEventByUpdateUserRequest(request, event);

        return eventMapper.toResponse(eventRepository.save(eventUpdate));
    }

    @Override
    public List<EventShortResponse> getAllEventsByOwner(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_ID_ASC);

        Page<Event> events = eventRepository.findAllByInitiatorId(userId, page);

        return eventMapper.toEventShortResponseCollection(events.getContent());

    }

    @Override
    public EventResponse getEventsByOwner(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        return eventMapper.toResponse(event);
    }

    @Override
    public List<ParticipationResponse> getRequestsByUser(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictServerError("Не является создателем Event");
        }

        List<Request> request = requestRepository.findByEventId(eventId, SORT_ID_ASC);

        return requestMapper.toParticipationResponseCollection(request);
    }

    @Override
    public EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictServerError("Для этого Event подтверждение не требуется");
        }

        int confirmedLimit = event.getParticipantLimit();
        int confirmedRequest = event.getConfirmedRequests();

            if (confirmedLimit <= confirmedRequest) {
                throw new ConflictServerError("Достигнут лимит заявок");
            }

        List<Request> requests = requestRepository
                .findAllById(eventRequestStatusUpdateRequest.getRequestIds());

        List<ParticipationResponse> confirmedRequests = new ArrayList<>();
        List<ParticipationResponse> rejectedRequests = new ArrayList<>();
        List<Request> requestsUpdate = new ArrayList<>();

        for (Request request: requests) {
            if (confirmedLimit > confirmedRequest && eventRequestStatusUpdateRequest.getStatus().equals(Status.CONFIRMED)) {

                if (request.getStatus().equals(Status.CONFIRMED)) {
                    throw new ConflictServerError("Заявка уже одобрена");
                }

                if (request.getStatus().equals(Status.CANCELED)) {
                    throw new ConflictServerError("Заявка уже отклонена");
                }

                if (request.getStatus().equals(Status.PENDING)) {
                    request.setStatus(Status.CONFIRMED);
                    requestsUpdate.add(request);
                    confirmedRequests.add(requestMapper.toParticipationResponse(request));
                    confirmedRequest++;
                }

            } else {

                if (request.getStatus().equals(Status.CONFIRMED)) {
                    throw new ConflictServerError("Заявка уже одобрена");
                }

                if (request.getStatus().equals(Status.CANCELED)) {
                    throw new ConflictServerError("Заявка уже отклонена");
                }

                if (request.getStatus().equals(Status.PENDING)) {
                    request.setStatus(Status.REJECTED);
                    requestsUpdate.add(request);
                    rejectedRequests.add(requestMapper.toParticipationResponse(request));
                }
            }

        }

        requestRepository.saveAll(requestsUpdate);

        event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequests.size());
        eventRepository.save(event);

        EventRequestStatusUpdateResult.EventRequestStatusUpdateResultBuilder eventRequestResult = EventRequestStatusUpdateResult.builder();
        eventRequestResult.confirmedRequests(confirmedRequests);
        eventRequestResult.rejectedRequests(rejectedRequests);
        return eventRequestResult.build();
    }
}
