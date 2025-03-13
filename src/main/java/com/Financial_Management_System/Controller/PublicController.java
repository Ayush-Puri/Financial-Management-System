package com.Financial_Management_System.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Financial_Management_System.DTO.AuthDTO;
import com.Financial_Management_System.DTO.UserDTO;
import com.Financial_Management_System.Service.JWTService;
import com.Financial_Management_System.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/signup")
    public ResponseEntity<String> saveUser(@Valid @RequestBody(required = false) UserDTO userDto) throws MethodArgumentNotValidException {
        return userService.saveUserDTO(userDto);
    }

    @PostMapping("/login")
    public String verifyUser(@RequestBody AuthDTO authDTO){

        Authentication authentication1 = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getUsername().toLowerCase(), authDTO.getPassword()));
        if(authentication1.isAuthenticated()){
            return jwtService.generateToken(authDTO.getUsername().toLowerCase());
        }
        return "fail";

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        String Result_part1 = userService.verifyUser(authDTO);
//        String Result = username+Result_part1;
//
//        return Result;
    }


}
