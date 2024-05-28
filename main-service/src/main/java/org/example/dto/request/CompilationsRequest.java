package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.createAndUpdate.Create;
import org.example.createAndUpdate.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class CompilationsRequest {

    private List<Long> events;

    private Boolean pinned;

    @NotBlank(groups = {Create.class})
    @Size(min = 1, max = 50, groups = {Create.class, Update.class})
    private String title;
}
