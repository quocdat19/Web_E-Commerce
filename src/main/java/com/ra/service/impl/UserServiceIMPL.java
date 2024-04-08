package com.ra.service.impl;

import com.ra.model.dto.request.UserLogin;
import com.ra.model.dto.request.UserRegister;
import com.ra.model.dto.response.JwtResponse;
import com.ra.model.entity.ERoles;
import com.ra.model.entity.Roles;
import com.ra.model.entity.Users;

import com.ra.repository.UserRepository;
import com.ra.sercurity.UserDetail.UserPrincipal;
import com.ra.sercurity.jwt.JwtProvider;
import com.ra.service.RoleService;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public JwtResponse handleLogin(UserLogin userLogin) {
        Authentication authentication;
        try {
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(),userLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("tài khoản hoặc mật khẩu sai");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if(!userPrincipal.getUsers().isStatus()) {
            throw new RuntimeException("your account is blocked");
        }
        return JwtResponse.builder()
                .accessToken(jwtProvider.generateToken(userPrincipal))
                .fullName(userPrincipal.getUsers().getFullName())
                .username(userPrincipal.getUsername())
                .status(userPrincipal.getUsers().isStatus())
                .roles(userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public String handleRegister(UserRegister userRegister) {

        if(userRepository.existsByUsername(userRegister.getUsername())) {
            throw new RuntimeException("username is exists");
        }

        Set<Roles> roles = new HashSet<>();
        roles.add(roleService.findByRoleName(ERoles.ROLE_USER));

        Users users = Users.builder()
                .fullName(userRegister.getFullName())
                .username(userRegister.getUsername())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .email(userRegister.getEmail())
                .avatar(userRegister.getAvatar())
                .phone(userRegister.getPhone())
                .address(userRegister.getAddress())
                .created(new Date(new java.util.Date().getTime()))
                .status(true)
                .roles(roles)
                .build();
        userRepository.save(users);
        return "Success";
    }

    @Override
    public Page<Users> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Users save(Users users) {
        return userRepository.save(users);
    }

    @Override
    public Users updateAcc(UserRegister userRegister, Long id) {
        if(userRepository.existsByUsername(userRegister.getUsername())) {
            throw new RuntimeException("username is exists");
        }

        Users userOld = findById(id);

        Set<Roles> roles = userOld.getRoles();

        Users users = Users.builder()
                .id(id)
                .fullName(userRegister.getFullName())
                .username(userRegister.getUsername())
                .password(userOld.getPassword())
                .email(userRegister.getEmail())
                .avatar(userRegister.getAvatar())
                .phone(userRegister.getPhone())
                .address(userRegister.getAddress())
                .created(userOld.getCreated())
                .updated(new Date(new java.util.Date().getTime()))
                .status(true)
                .roles(roles)
                .build();
        return userRepository.save(users);
    }

    @Override
    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<Users> SearchByFullName(String keyword) {
        return userRepository.searchAllByUAndFullName(keyword);
    }

}
