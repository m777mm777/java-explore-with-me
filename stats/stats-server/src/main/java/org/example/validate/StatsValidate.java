package org.example.validate;

import org.example.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StatsValidate {
    public boolean validateTime(LocalDateTime start,  LocalDateTime end) {

        if (start == null || end == null) {
            throw new ValidationException("Отсутствует одна из дат (начало/Окончание)");
        }

        if (start.isAfter(end)) {
            throw new ValidationException("Дата окончания раньше даты начала");
        }

        if (start.equals(end)) {
            throw new ValidationException("Дата окончания брони та же что и начала");
        }

        return true;
    }
}