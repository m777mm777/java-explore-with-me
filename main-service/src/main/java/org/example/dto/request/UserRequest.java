package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.createAndUpdate.Create;
import org.example.createAndUpdate.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserRequest {

    @Size(min = 2, max = 250, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private String name;

    @Size(min = 6, max = 254, groups = {Create.class, Update.class})
    @NotEmpty(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;

}
