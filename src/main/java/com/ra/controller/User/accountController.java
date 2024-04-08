package com.ra.controller.User;

import com.ra.model.dto.request.AddressRequest;
import com.ra.model.dto.request.PasswordRequest;
import com.ra.model.dto.request.UserRegister;
import com.ra.model.entity.Address;
import com.ra.model.entity.Users;
import com.ra.sercurity.UserDetail.UserPrincipal;
import com.ra.service.AddressService;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user/account")
public class accountController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressService addressService;

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userPrincipal.getUsers().getId();
    }

    @GetMapping("")
    public ResponseEntity<Users> getAccount() {
        Long id = getUserId();
        Users user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Users> updateAccount(@RequestBody UserRegister userRegister) {
        Long id = getUserId();
        Users user = userService.updateAcc(userRegister, id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordRequest passwordRequest) {
        Long id = getUserId();
        Users user = userService.findById(id);
        String oldPassword = user.getPassword();

        boolean isPaswordMatch = passwordEncoder.matches(passwordRequest.getOldPass(), oldPassword);
        if (isPaswordMatch) {
            if (!passwordRequest.getNewPass().equals(passwordRequest.getConfirmNewPass())){
                return new ResponseEntity<>("Xác nhận mật khẩu không chính xác!", HttpStatus.BAD_REQUEST);
            }
             user.setPassword(passwordEncoder.encode(passwordRequest.getNewPass()));
             userService.save(user);
             return new ResponseEntity<>("cập nhật mật khẩu thành công", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Mật khẩu cũ không chính xác!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/address")
    public ResponseEntity<?> addAdress(@RequestBody AddressRequest addressRequest) {
        Long id = getUserId();
        Address address = addressService.add(addressRequest, id);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long addressId) {
        Long id = getUserId();
        addressService.delete(addressId, id);
        return new ResponseEntity<>("địa chỉ đã xóa khỏi tài khỏa của bạn!" ,HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> findAllAddress() {
        Long id = getUserId();
        List<Address> addresses = addressService.getAll(id);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<Address> findById(@PathVariable("id") Long addressId) {
        Long id = getUserId();
        Address address = addressService.findById(addressId, id);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }
}
