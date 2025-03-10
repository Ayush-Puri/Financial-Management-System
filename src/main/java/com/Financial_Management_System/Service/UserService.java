package com.Financial_Management_System.Service;

import com.Financial_Management_System.DTO.AuthDTO;
import com.Financial_Management_System.DTO.UserDTO;
import com.Financial_Management_System.DTO.UserReadDTO;
import com.Financial_Management_System.Entity.UserEntity;
import com.Financial_Management_System.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private UserRepository userRepository;

    public List<UserReadDTO> findAll(){

        List<UserEntity> list_of_All_Users =   userRepository.findAll();
        return list_of_All_Users.stream()
                .map(userFound -> UserReadDTO.builder()
                        .username(userFound.getUsername())
                        .category(userFound.getCategory())
                        .wallet(userFound.getWallet())
                        .email(userFound.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public UserReadDTO findUser() throws Exception{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userFound = findUserEntityByUsername(username).get();

            return UserReadDTO.builder()
                    .username(userFound.getUsername())
                    .email(userFound.getEmail())
                    .category(userFound.getCategory())
                    .wallet(userFound.getWallet())
                    .build();
    }

    public Optional<UserEntity> findUserEntityByUsername(String username) throws Exception{
        Optional<UserEntity> userFound = userRepository.findByUsername(username);
        if(userFound.isPresent()){
            return userFound;
        }
        else throw new Exception("User Not Found in database");
    }

    public ResponseEntity<String> saveUserDTO(UserDTO user){

        Set<String> category = new HashSet<>();
        category.add("food");
        category.add("rent");
        category.add("entertainment");
        category.add("Uncategorized");

        UserEntity newUser = UserEntity.builder()
                .username(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail())
                .category(category)
                .phone(user.getPhone())
                .wallet(0.0)
                .savinggoals(new HashSet<>())
                .transactionList(new ArrayList<>())
                .build();
        userRepository.save(newUser);
        return ResponseEntity.ok("User Successfully Saved.");
    }

    public String deleteUserEntity(String username) throws Exception {
    Optional<UserEntity> toBeDeletedUser = findUserEntityByUsername(username);
    if(toBeDeletedUser.isPresent()) {
        userRepository.delete(toBeDeletedUser.get());
        return "Deletion Successful";
    }else return "Deletion Insuccessful";
    }

    public List<UserReadDTO> findAllUserEntity(){
        return userRepository.findAll().stream().map(
                user -> UserReadDTO.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .category(user.getCategory())
                        .wallet(user.getWallet())
                        .build())
                .collect(Collectors.toList());
    }

    public String verifyUser(AuthDTO authDTO){
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.getUsername(), authDTO.getPassword()));

        if(authentication.isAuthenticated())
            return "Authenicated";
        return "Authentication Failed";
    }

    public boolean isUserPresentinDatabase(Long userId){
        return userRepository.findById(userId).isPresent();
    }

    public String deleteUserEntitybyID(Long userId){
        if(isUserPresentinDatabase(userId)){
            userRepository.deleteById(userId);
            return "Deletion Successful";
        }
        return "Deletion Unsuccessful";
    }

    public UserReadDTO updateUser(UserDTO user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity currentUser = findUserEntityByUsername(username).get();

        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        currentUser.setUsername(user.getUsername());

        userRepository.save(currentUser);

        return UserReadDTO.builder()
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .wallet(currentUser.getWallet())
                .category(currentUser.getCategory())
                .build();

    }
}
