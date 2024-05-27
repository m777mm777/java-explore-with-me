package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.createAndUpdate.Create;
import org.example.createAndUpdate.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @Size(min = 1, max = 50, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private String name;
}
