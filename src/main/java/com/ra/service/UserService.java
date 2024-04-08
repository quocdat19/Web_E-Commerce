package com.ra.service;

import com.ra.model.dto.request.UserLogin;
import com.ra.model.dto.request.UserRegister;
import com.ra.model.dto.response.JwtResponse;
import com.ra.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    JwtResponse handleLogin(UserLogin userLogin);
    String handleRegister(UserRegister userRegister);
    Page<Users> getAll(Pageable pageable);
    Users findById(Long id);
    void delete(Long id);
    Users save(Users users);
    Users updateAcc(UserRegister userRegister, Long id);
    Optional<Users> findByUsername(String username);
    List<Users> SearchByFullName(String keyword);
}
