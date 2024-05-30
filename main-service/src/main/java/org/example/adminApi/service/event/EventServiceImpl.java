package org.example.adminApi.service.event;

import lombok.RequiredArgsConstructor;
import org.example.adminApi.repository.CategoryRepository;
import org.example.adminApi.repository.EventRepositoryAdmin;
import org.example.dto.request.EventSearchFilter;
import org.example.dto.request.UpdateEventAdminRequest;
import org.example.dto.response.CommentResponse;
import org.example.dto.response.EventResponse;
import org.example.enums.State;
import org.example.exceptions.ConflictServerError;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.ValidationException;
import org.example.mapper.CommentMapper;
import org.example.mapper.EventMapper;
import org.example.model.Category;
import org.example.model.Comment;
import org.example.model.Event;
import org.example.privateApi.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepositoryAdmin eventRepositoryAdmin;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private static final Sort SORT_ID_ASC = Sort.by(Sort.Direction.ASC, "id");

    @Override
    public EventResponse updateEvent(Long eventId, UpdateEventAdminRequest request) {

        Event event = eventRepositoryAdmin.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        if (request.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ConflictServerError("Событие можно публиковать/отклонить, только если оно в состоянии ожидания");
            }
        }

        if (request.getEventDate() != null) {
            if (!request.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
                    throw new ValidationException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
        }

        event = eventMapper.toEventByUpdateAdminRequest(request, event);

        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new ResourceNotFoundException("Category не найдена"));

            event.setCategory(category);
        }

        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        List<CommentResponse> commentResponses = commentMapper.toResponseCollection(comments);

        EventResponse eventResponse = eventMapper.toResponse(eventRepositoryAdmin.save(event));
        eventResponse.setCommentResponses(commentResponses);
        return eventResponse;
    }

    @Override
    public List<EventResponse> getEventsByFilter(List<Long> users,
                                         List<State> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Integer from,
                                         Integer size) {

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_ID_ASC);
        List<EventResponse> eventResponses = new ArrayList<>();
        EventSearchFilter eventSearchFilter = new EventSearchFilter();

        if (users != null) {
            if (users.get(0) != 0) {
                eventSearchFilter.setUsers(users);
            }
        }

        eventSearchFilter.setStates(states);

        if (categories != null) {
            if (categories.get(0) != 0) {
                eventSearchFilter.setCategories(categories);
            }
        }

        eventSearchFilter.setRangeStart(rangeStart);
        eventSearchFilter.setRangeEnd(rangeEnd);

        List<Specification<Event>> specifications = searchFilterToSpecifications(eventSearchFilter);

        Page<Event> eventPage = eventRepositoryAdmin.findAll(
                specifications.stream().reduce(Specification::and).orElse(null), page
        );

        if (!eventPage.hasContent()) {
            return new ArrayList<>();
        }

        List<Event> events = eventPage.getContent();

        Map<Event, List<Comment>> comments = commentRepository.findAllByEventIn(events)
                .stream()
                .collect(groupingBy(Comment::getEvent, toList()));

        if (!comments.isEmpty()) {

            for (Event event : events) {
                EventResponse eventResponse = eventMapper.toResponse(event);
                eventResponse.setCommentResponses(commentMapper.toResponseCollection(comments.get(event)));

                eventResponses.add(eventResponse);
            }
        } else {
            eventResponses = eventMapper.toResponseCollection(events);
        }

        return eventResponses;
    }

    private List<Specification<Event>> searchFilterToSpecifications(EventSearchFilter eventSearchFilter) {

        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(eventSearchFilter.getUsers() == null ? null : usersId(eventSearchFilter.getUsers()));
        specifications.add(eventSearchFilter.getStates() == null ? null : states(eventSearchFilter.getStates()));
        specifications.add(eventSearchFilter.getCategories() == null ? null : categoriesId(eventSearchFilter.getCategories()));
        specifications.add(eventSearchFilter.getRangeStart() == null ? null : eventAfter(eventSearchFilter.getRangeStart()));
        specifications.add(eventSearchFilter.getRangeEnd() == null ? null : eventBefore(eventSearchFilter.getRangeEnd()));
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Event> usersId(List<Long> values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(values);
    }

    private Specification<Event> states(List<State> values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(values);
    }

    private Specification<Event> categoriesId(List<Long> values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(values);
    }

    private Specification<Event> eventAfter(LocalDateTime values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), values);
    }

    private Specification<Event> eventBefore(LocalDateTime values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), values);
    }
}
