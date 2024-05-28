package org.example.adminApi.service.user;

import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserRequest userRequest);

    List<UserResponse> getAll(List<Long> ids, Integer from, Integer size);

    Long remove(Long userId);
}
