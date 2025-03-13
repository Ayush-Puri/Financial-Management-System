package com.Financial_Management_System.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Financial_Management_System.DTO.TransactionDTO;
import com.Financial_Management_System.DTO.TransactionReturnDTO;
import com.Financial_Management_System.DTO.TransactionType;
import com.Financial_Management_System.Entity.UserEntity;
import com.Financial_Management_System.Entity.userTransaction;
import com.Financial_Management_System.Repository.TransactionRepository;
import com.Financial_Management_System.Repository.UserRepository;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    public String saveTransaction(TransactionDTO transactionDTO, String username) throws Exception {
        // Fetch user by ID
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return "User not found.";
        }
        // Create new transaction
        userTransaction transaction = userTransaction.builder()
                .user(user.get())
                .username(username)
                .amount(transactionDTO.getAmount())
                .category(transactionDTO.getCategory().toLowerCase().isBlank() ? "uncategorized":transactionDTO.getCategory().toLowerCase())
                .type(transactionDTO.getType())
                .dateTime(LocalDateTime.now())
                .description(transactionDTO.getDescription())
                .build();

        // Save transaction
        transactionRepository.save(transaction);

        int multiplier = 0;
        if(transactionDTO.getType()== TransactionType.expense){
            multiplier = -1;
        }else multiplier = 1;

        // Update user's wallet
        double updatedWallet = user.get().getWallet()  + transactionDTO.getAmount()*multiplier;
        user.get().setWallet(updatedWallet);

        if(!user.get().getCategory().contains(transactionDTO.getCategory().toLowerCase())){

            Set<String> newCategoryList = user.get().getCategory();
            newCategoryList.add(transactionDTO.getCategory().toLowerCase());
            user.get().setCategory(newCategoryList);
        }

        userRepository.save(user.get());

        return "Transaction saved successfully.";
    }

    public List<TransactionReturnDTO> findAllTransactionsByUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return transactionRepository.findAllByUsername(username).stream()
                .map(transaction -> TransactionReturnDTO.builder()
                        .amount(transaction.getAmount())
                        .dateTime(transaction.getDateTime())
                        .transactionid(transaction.getTransactionid())
                        .type(transaction.getType())
                        .username(transaction.getUsername())
                        .category(transaction.getCategory())
                        .description(transaction.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public TransactionReturnDTO updateTransaction(TransactionDTO transactionDTO, Integer id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findUserEntityByUsername(username).get();

        Optional<userTransaction> transaction = transactionRepository.findByTransactionid(id);
        if(transaction.isEmpty()) throw new Exception("Transaction Not Found");
        if(!transaction.get().getUsername().equals(currentUser.getUsername()))
            throw new Exception("Transaction is not Your to edit");

        Double previousAmt = transaction.get().getAmount();

        transaction.get().setAmount(transactionDTO.getAmount());
        transaction.get().setCategory(transactionDTO.getCategory().toLowerCase());
        transaction.get().setDescription(transactionDTO.getDescription());
        transaction.get().setType(transactionDTO.getType());

        if(!currentUser.getCategory().contains(transactionDTO.getCategory())){
            Set<String> newCategoryList = currentUser.getCategory();
            newCategoryList.add(transactionDTO.getCategory());
            currentUser.setCategory(newCategoryList);
        }

        currentUser.setWallet(currentUser.getWallet()+ transactionDTO.getAmount()-previousAmt);
        userRepository.save(currentUser);
        transactionRepository.save(transaction.get());

        return TransactionReturnDTO.builder()
                .transactionid(transaction.get().getTransactionid())
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .category(transactionDTO.getCategory())
                .username(currentUser.getUsername())
                .description(transactionDTO.getDescription())
                .dateTime(transaction.get().getDateTime())
                .build();
    }

}
