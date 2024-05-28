package org.example.publicApi.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.StatsClient;
import com.google.gson.Gson;
import org.example.dto.HitRequest;
import org.example.dto.StatsResponse;
import org.example.dto.request.EventSearchFilterPublic;
import org.example.dto.response.EventResponse;
import org.example.dto.response.EventShortResponse;
import org.example.enums.State;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.ValidationException;
import org.example.mapper.EventMapper;
import org.example.model.Event;
import org.example.publicApi.repository.EventPublicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {

    private final EventPublicRepository eventPublicRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    private final Gson gson;
    private static final Sort SORT_EVENT_DATE_ASC = Sort.by(Sort.Direction.ASC, "eventDate");
    private static final Sort SORT_VIEWS_ASC = Sort.by(Sort.Direction.ASC, "views");
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortResponse> getAllEvents(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable,
                                                 String sort,
                                                 Integer from,
                                                 Integer size,
                                                 HttpServletRequest httpServletRequest) {


        EventSearchFilterPublic eventSearchFilterPublic = new EventSearchFilterPublic();

        if (text != null) {
            if (text.equals("0")) {
                throw new ValidationException("Валидация не пройдена");
            }
        }

        if (categories != null) {
            if (categories.get(0) == 0) {
                throw new ValidationException("Валидация не пройдена");
            }
        }

        eventSearchFilterPublic.setText(text);
        eventSearchFilterPublic.setCategories(categories);
        eventSearchFilterPublic.setPaid(paid);
        eventSearchFilterPublic.setRangeStart(rangeStart);
        eventSearchFilterPublic.setRangeEnd(rangeEnd);
        eventSearchFilterPublic.setOnlyAvailable(onlyAvailable);

        List<Specification<Event>> specifications = searchFilterToSpecifications(eventSearchFilterPublic);

        Pageable page = PageRequest.of(from, size);

        if (sort.equals("EVENT_DATE")) {
            page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_EVENT_DATE_ASC);
        } else if (sort.equals("VIEWS")) {
            page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_VIEWS_ASC);
        }

        Page<Event> eventPage = eventPublicRepository.findAll(
                specifications.stream().reduce(Specification::and).orElse(null), page
        );

        List<Event> events = eventPage.getContent();

        for (Event event: events) {
            event.setViews(event.getViews() + 1);
        }

        eventPublicRepository.saveAll(events);

        addStatistic(httpServletRequest);

        return eventMapper.toEventShortResponseCollection(events);
    }

    @Override
    public EventResponse getEvent(Long id, HttpServletRequest httpServletRequest) {
        Event event = eventPublicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ResourceNotFoundException("Event не опубликован");
        }

        addStatistic(httpServletRequest);

        Map<Long, Long> views = getEventsViews(List.of(id));

        event.setViews(views.get(id));

        eventPublicRepository.save(event);

        return eventMapper.toResponse(event);
    }

    private void addStatistic(HttpServletRequest httpServletRequest) {

        HitRequest endpointHit = new HitRequest();
        endpointHit.setIp(httpServletRequest.getRemoteAddr());
        endpointHit.setUri(httpServletRequest.getRequestURI());
        endpointHit.setApp("ewm-main-service");
        endpointHit.setTimestamp(LocalDateTime.now());

        statsClient.addStatistic(endpointHit);
    }

    private List<Specification<Event>> searchFilterToSpecifications(EventSearchFilterPublic eventSearchFilterPublic) {

        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(eventSearchFilterPublic.getText() == null ? null : text(List.of(eventSearchFilterPublic.getText())));
        specifications.add(eventSearchFilterPublic.getCategories() == null ? null : categoriesId(eventSearchFilterPublic.getCategories()));
        specifications.add(eventSearchFilterPublic.getPaid() == null ? null : isPaid(eventSearchFilterPublic.getPaid()));
        specifications.add(eventSearchFilterPublic.getRangeStart() == null ? null : eventAfter(eventSearchFilterPublic.getRangeStart()));
        specifications.add(eventSearchFilterPublic.getRangeEnd() == null ? null : eventBefore(eventSearchFilterPublic.getRangeEnd()));
        specifications.add(eventSearchFilterPublic.getOnlyAvailable() == null ? null : isOnlyAvailable(eventSearchFilterPublic.getOnlyAvailable()));
        specifications.add(eventSearchFilterPublic.getState() == null ? null : states(eventSearchFilterPublic.getState()));
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Event> text(List<String> values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.get("annotation"), values),
                criteriaBuilder.equal(root.get("description"), values));
    }

    private Specification<Event> categoriesId(List<Long> values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(values);
    }

    private Specification<Event> isPaid(Boolean values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), values);
    }

    private Specification<Event> eventAfter(LocalDateTime values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), values);
    }

    private Specification<Event> eventBefore(LocalDateTime values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), values);
    }

    private Specification<Event> isOnlyAvailable(Boolean values) {
        if (values) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("confirmedRequests"),
                    root.get("participantLimit"));
        }
        return null;
    }

    private Specification<Event> states(State values) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(values);
    }

    public Map<Long, Long> getEventsViews(List<Long> events) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<StatsResponse> stats;
        Map<Long, Long> eventsViews = new HashMap<>();
        List<String> uris = new ArrayList<>();

        if (events == null || events.isEmpty()) {
            return eventsViews;
        }
        for (Long id : events) {
            uris.add("/events/" + id);
        }
        ResponseEntity<Object> response = statsClient.getStatistic(LocalDateTime.now().minusDays(100).format(FORMAT),
                LocalDateTime.now().format(FORMAT), uris, true);
        Object body = response.getBody();
        if (body != null) {
            String json = gson.toJson(body);
            TypeReference<List<StatsResponse>> typeRef = new TypeReference<>() {
            };
            try {
                stats = objectMapper.readValue(json, typeRef);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Ошибка при загрузке данных из сервиса статистики");
            }
            for (Long event : events) {
                eventsViews.put(event, 0L);
            }
            if (!stats.isEmpty()) {
                for (StatsResponse stat : stats) {
                    eventsViews.put(Long.parseLong(stat.getUri().split("/", 0)[2]),
                            stat.getHits());
                }
            }
        }
        return eventsViews;
    }
}
