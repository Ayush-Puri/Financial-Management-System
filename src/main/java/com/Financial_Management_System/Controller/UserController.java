package com.Financial_Management_System.Controller;

import com.Financial_Management_System.DTO.UserDTO;
import com.Financial_Management_System.DTO.UserReadDTO;
import com.Financial_Management_System.Entity.UserEntity;
import com.Financial_Management_System.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/Allusers")
    public List<UserReadDTO> findAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/user")
    public UserReadDTO findUser() throws Exception {
        return userService.findUser();
    }

    @GetMapping("/allusers")
    public List<UserReadDTO> findAllUserEntity(){
        return userService.findAllUserEntity();
    }

    @DeleteMapping("/user")
    public String deleteUserEntity() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findUserEntityByUsername(username).get();
        return userService.deleteUserEntity(currentUser.getUsername());
    }

    @GetMapping("/MyCategories")
    public Set<String> findAllCategoriesOfUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findUserEntityByUsername(username).get();

        return currentUser.getCategory();
    }

    @PutMapping("/user")
    public UserReadDTO updateUser(@RequestBody UserDTO user) throws Exception {
        return userService.updateUser(user);
    }

}