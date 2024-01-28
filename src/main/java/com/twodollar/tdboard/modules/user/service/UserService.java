package com.twodollar.tdboard.modules.user.service;

import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.entity.enums.RoleEnum;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public long getTotalUsersSize(){
        return userRepository.count();
    }
    public List<User> getUsers(Pageable pageable){
        List<User> list = userRepository.getUsersByDelYnOrderByCreatedAtDesc("N", pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "조회된 사용자가 없습니다."));
        return list;
    }

    public long getTotalUsersSizeByRole(RoleEnum role){
        return userRepository.countUsersByRole(role);
    }
    public List<User> getUsers(RoleEnum role, Pageable pageable){
        List<User> list = userRepository.getUsersByDelYnAndRoleOrderByCreatedAtDesc("N", role, pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "조회된 사용자가 없습니다."));
        return list;
    }

    public User getUserById(final Long userId) throws ResponseStatusException {
        return userRepository.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다."));
    }

    public User getUserByUserId(final String userId) throws ResponseStatusException {
        return userRepository.findByUserId(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다."));
    }

    public User update(String userId, UserRequest userRequest) throws ResponseStatusException{
        User user = this.getUserByUserId(userId);
        user.update(userRequest);
        userRepository.save(user);
        return user;
    }
    public User memberUpdate(Long userId, UserRequest userRequest) throws ResponseStatusException{
        User user = this.getUserById(userId);
        user.memberUpdate(userRequest);
        return userRepository.save(user);
    }

    public User delete(Long userId) throws ResponseStatusException{
        User user = this.getUserById(userId);
        user.sucession();// 회원탈퇴 처리
        return userRepository.save(user);
    }
}
