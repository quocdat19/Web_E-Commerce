package com.ra.controller.Admin;

import com.ra.model.entity.Roles;
import com.ra.model.entity.Users;
import com.ra.service.RoleService;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/admin")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "username", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }
        Page<Users> users = userService.getAll(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("users/{userId}")
    public ResponseEntity<?> updateStatusUser(@PathVariable("userId") Long id) {
        Users user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>("người dùng không tồn tại", HttpStatus.BAD_REQUEST);
        } else {
            user.setStatus(!user.isStatus());
            userService.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        List<Roles> roles = roleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("users/search")
    public ResponseEntity<?> findByName(@RequestParam(name = "name") String query) {
        List<Users> user = userService.SearchByFullName(query);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
