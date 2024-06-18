package com.example.shopapp.controller;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.servies.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
            try{
                if(result.hasErrors()){
                    List<String> errorMessages = result.getFieldErrors()
                            .stream()
                            .map(FieldError::getDefaultMessage)
                            .toList();
                    return ResponseEntity.badRequest().body(errorMessages);
                }
                if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                    return ResponseEntity.badRequest().body("password khong trung nhau");
                }
                iUserService.createUser(userDTO);
                return ResponseEntity.ok("Register Successfully!");
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userloginDTO){
            // Kiểm tra thông tin đăng nhập và gen token
        String token = iUserService.login(userloginDTO.getPhoneNumber(), userloginDTO.getPassword());
            // trả về token trong response
        return ResponseEntity.ok(token);
    }

}
