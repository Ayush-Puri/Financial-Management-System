package com.syfe.jan19test3.Service;

import com.syfe.jan19test3.DTO.AuthDTO;
import com.syfe.jan19test3.DTO.UserDTO;
import com.syfe.jan19test3.Entity.UserEntity;
import com.syfe.jan19test3.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    @Autowired
    private UserRepository userRepo;

    public List<UserDTO> findAllUserDTO(){

        List<UserEntity> list_of_All_Users =   userRepo.findAll();
        return list_of_All_Users.stream()
                .map(user -> UserDTO.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .build())
                .collect(Collectors.toList());
    }

    public UserDTO findUserDTO(Long Id) throws Exception{
        Optional<UserEntity> userFound = userRepo.findById(Id);
        if(userFound.isPresent()){
            return UserDTO.builder()
                    .email(userFound.get().getEmail())
                    .username(userFound.get().getUsername())
                    .password(userFound.get().getPassword())
                    .build();
        }
        throw new Exception("User Not Found");
    }

    public UserDTO findUserDTO(String username, String password) throws Exception{
        Optional<UserEntity> userFound = userRepo.findByUsername(username);
        if(userFound.isPresent() && userFound.get().getPassword().equalsIgnoreCase(encoder.encode(password))){
            return UserDTO.builder()
                    .email(userFound.get().getEmail())
                    .username(userFound.get().getUsername())
                    .password(userFound.get().getPassword())
                    .build();
        }
        throw new Exception("User Not Found");
    }


    public Optional<UserEntity> findUserEntity(String username, String password) throws Exception{
        Optional<UserEntity> userFound = userRepo.findByUsername(username);
        if(userFound.isPresent() && userFound.get().getPassword().equalsIgnoreCase(encoder.encode(password))){
            return userFound;
        }
        else if(userFound.isPresent() && !userFound.get().getPassword().equalsIgnoreCase(password)){
            throw new Exception("User Found but Password Incorrect");
        }
        else throw new Exception("User Not Found in database");
    }

    public String saveUserDTO(UserDTO user){

        Set<String> category = new HashSet<>();
        category.add("food");
        category.add("rent");
        category.add("entertainment");

        UserEntity newUser = UserEntity.builder()
                .username(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail())
                .category(category)
                .transactionList(new ArrayList<>())
                .build();
        userRepo.save(newUser);
        return "User Successfully Saved!";
    }

    public String deleteUserEntity(String username, String password) throws Exception {
    Optional<UserEntity> deletedUser = findUserEntity(username, password);
    if(deletedUser.isPresent()) {
        userRepo.deleteById(deletedUser.get().getUserid());
        return "Deletion Successful";
    }else return "Deletion Insuccessful";
    }

    public List<UserEntity> findAllUserEntity(){
        return userRepo.findAll();
    }

    public String verifyUser(AuthDTO authDTO){
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.getUsername(), authDTO.getPassword()));

        if(authentication.isAuthenticated())
            return "Authenicated";

        return "Authentication Failed";
    }

}
