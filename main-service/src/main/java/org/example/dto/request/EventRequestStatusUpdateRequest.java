package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private Status status;
}
