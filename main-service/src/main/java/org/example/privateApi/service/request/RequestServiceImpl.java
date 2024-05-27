package org.example.privateApi.service.request;

import lombok.RequiredArgsConstructor;
import org.example.adminApi.repository.UserRepository;
import org.example.dto.response.ParticipationResponse;
import org.example.enums.State;
import org.example.enums.Status;
import org.example.exceptions.ConflictServerError;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.RequestMapper;
import org.example.model.Event;
import org.example.model.Request;
import org.example.model.User;
import org.example.privateApi.repository.EventRepository;
import org.example.privateApi.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;

    @Override
    public ParticipationResponse create(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        checkingBeforeRegistration(user, event);

        Request request = requestMapper.toRequestByEvent(event, user);
        request.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }


        Request saveRequest = requestRepository.save(request);
        return requestMapper.toParticipationResponse(saveRequest);
    }

    @Override
    public List<ParticipationResponse> getAllRequestUser(Long userId) {
        List<Request> requests = requestRepository.findByRequesterId(userId);
        return requestMapper.toParticipationResponseCollection(requests);
    }

    @Override
    public ParticipationResponse cancelRequestByRequester(Long userId, Long requestId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Request request = requestRepository.findByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request не найден"));

        request.setStatus(Status.CANCELED);

        return requestMapper.toParticipationResponse(requestRepository.save(request));
    }

    private void checkingBeforeRegistration(User requester, Event event) {

        if (requester.getId().equals(event.getInitiator().getId())) {
            throw new ConflictServerError("Участие в своем Event не допустимо");
        }

        Optional<Request> request = requestRepository.findByRequesterIdAndEventId(requester.getId(), event.getId());

        if (request.isPresent()) {
            throw new ConflictServerError("Пользователь уже подал заявку на этот Event");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictServerError("Event не опубликован администратором");
        }

        if (event.getParticipantLimit() != 0) {
            if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                throw new ConflictServerError("Достигнут лимит пользователей");
            }
        }

    }

}
