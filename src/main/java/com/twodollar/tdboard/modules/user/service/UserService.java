package com.twodollar.tdboard.modules.user.service;

import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUserId(final String userId) throws ResponseStatusException {
        return userRepository.findByUserId(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다."));
    }

    public User update(String userId, UserRequest userRequest) throws ResponseStatusException{
        User user = this.getUserByUserId(userId);
        user.update(userRequest);
        userRepository.save(user);

        return user;
    }
}
