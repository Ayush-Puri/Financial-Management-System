package com.Financial_Management_System.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Financial_Management_System.DTO.AuthDTO;
import com.Financial_Management_System.DTO.UserDTO;
import com.Financial_Management_System.DTO.UserReadDTO;
import com.Financial_Management_System.Entity.UserEntity;
import com.Financial_Management_System.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private JWTService JWTService;

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
        category.add("uncategorized");

        UserEntity newUser = UserEntity.builder()
                .username(user.getUsername().toLowerCase())
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail().toLowerCase())
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

    public UserReadDTO updateUser(UserDTO UpdateUser) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity currentUser = findUserEntityByUsername(username).get();

        // TODO: Add a check for the email to be unique by indicing the email column in the database
        if(UpdateUser.getEmail()!=null && !UpdateUser.getEmail().equals(currentUser.getEmail())){
            currentUser.setEmail(UpdateUser.getEmail().toLowerCase());
        }
        // TODO: Add a check for the pphone to be unique by indicing the phone column in the database
        if(UpdateUser.getPhone()!=null && !UpdateUser.getPhone().equals(currentUser.getPhone())){
            currentUser.setPhone(UpdateUser.getPhone());
        }
        if(UpdateUser.getPassword()!=null && !encoder.encode(UpdateUser.getPassword()).equals(currentUser.getPassword())){
            currentUser.setPassword(encoder.encode(UpdateUser.getPassword()));
        }

        if(UpdateUser.getUsername()!=null &&  !UpdateUser.getUsername().equals(currentUser.getUsername())){
            
            //if Username already exists, throw error message    
            if(userRepository.findByUsername(UpdateUser.getUsername()).isPresent()){
               throw new Exception("Username already exists"); 
            }
            currentUser.setUsername(UpdateUser.getUsername().toLowerCase());
        }



        userRepository.save(currentUser);

        return UserReadDTO.builder()
                .username(currentUser.getUsername())
                .phone(currentUser.getPhone())
                .email(currentUser.getEmail())
                .wallet(currentUser.getWallet())
                .category(currentUser.getCategory())
                .build();

    }
}
