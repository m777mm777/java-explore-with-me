package org.example.adminApi.service.user;

import lombok.RequiredArgsConstructor;
import org.example.adminApi.repository.UserRepository;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.exceptions.ConflictServerError;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Sort SORT_ID_ASC = Sort.by(Sort.Direction.ASC, "id");

    @Override
    public UserResponse create(UserRequest userRequest) {
        User user;

        try {
            user = userRepository.save(userMapper.toUser(userRequest));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictServerError("Такая почта уже есть");
        }

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll(List<Long> ids, Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_ID_ASC);

        List<User> users;

        if (ids == null) {
            Page<User> usersPage = userRepository.findAll(page);
            users = usersPage.getContent();
        } else {
            users = userRepository.findByIdIn(ids, page);
        }

        return userMapper.toResponseCollection(users);
    }

    @Override
    public Long remove(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        userRepository.deleteById(userId);
        return userId;
    }
}
