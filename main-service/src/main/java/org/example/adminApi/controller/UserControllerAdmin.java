package org.example.adminApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.adminApi.service.user.UserService;
import org.example.createAndUpdate.Create;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {

    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse crateUser(@Validated(Create.class) @RequestBody UserRequest request) {
        log.info("create User {}", request);
        return userService.create(request);
    }

    @GetMapping()
    public List<UserResponse> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                          @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                          @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GetAllUsers ids {} from {} size {}", ids, from, size);
        return userService.getAll(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long removeById(@PathVariable Long userId) {
        log.info("removeById userId {}", userId);
        return userService.remove(userId);
    }
}
