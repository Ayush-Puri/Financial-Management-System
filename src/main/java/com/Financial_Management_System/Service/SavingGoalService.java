package com.Financial_Management_System.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Financial_Management_System.DTO.SavingGoalDTO;
import com.Financial_Management_System.DTO.SavingGoal_ReturnDTO;
import com.Financial_Management_System.Entity.SavingGoal;
import com.Financial_Management_System.Entity.UserEntity;
import com.Financial_Management_System.Repository.SavingGoalRepository;

@Service
public class SavingGoalService {
    @Autowired
    private SavingGoalRepository savingGoalRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    private UserService userService;

    public List<SavingGoal_ReturnDTO> findAll() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findUserEntityByUsername(username).get();

        return savingGoalRepository.findAllByUsername(username).stream()
                .map(goal -> SavingGoal_ReturnDTO
                        .builder()
                        .savinggoalid(goal.getSavinggoalid())
                        .amountdifference(goal.getTargetamount()-currentUser.getWallet() > 0 ? goal.getTargetamount()-currentUser.getWallet():0)
                        .targetamount(goal.getTargetamount())
                        .targetdate(goal.getTargetdate())
                        .creationdate(goal.getCreationdate())
                        .daysremaining(Period.between(LocalDate.now(), goal.getTargetdate()))
                        .iscompleted(goal.getTargetamount() - currentUser.getWallet() < 0)
                        .username(goal.getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    public SavingGoal_ReturnDTO saveNewGoal(SavingGoalDTO goal) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findUserEntityByUsername(username).get();

        savingGoalRepository.save(SavingGoal.builder()
                        .user(currentUser)
                        .targetamount(goal.getTargetamount())
                        .targetdate(LocalDate.parse(goal.getTargetdate(), formatter))
                        .amountdifference(goal.getTargetamount()-currentUser.getWallet())
                        .iscompleted(goal.getTargetamount()-currentUser.getWallet() < 0)
                        .username(currentUser.getUsername())
                        .daysremaining(Period.between(LocalDate.now(),LocalDate.parse(goal.getTargetdate(), formatter)))
                        .creationdate(LocalDate.now())
                .build());

        return SavingGoal_ReturnDTO.builder()
                .savinggoalid(0)
                .amountdifference(goal.getTargetamount()-currentUser.getWallet())
                .targetdate(LocalDate.parse(goal.getTargetdate(), formatter))
                .creationdate(LocalDate.now())
                .daysremaining(Period.between(LocalDate.now(), LocalDate.parse(goal.getTargetdate(), formatter)))
                .iscompleted(goal.getTargetamount()-currentUser.getWallet() < 0)
                .username(currentUser.getUsername())
                .targetamount(goal.getTargetamount())
                .build();
    }

    public SavingGoal_ReturnDTO updateGoal(SavingGoalDTO goal, Integer goalId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findUserEntityByUsername(username).get();
        Optional<SavingGoal> savedgoal = savingGoalRepository.findBySavinggoalid(goalId);
        if(savedgoal.isEmpty()){
            throw new Exception("Goal Not Found");
        }
        if(!savedgoal.get().getUsername().equals(currentUser.getUsername())){
            throw new Exception("Saving Goal is not Your to Edit!");
        }
        savedgoal.get().setTargetamount(goal.getTargetamount());
        savedgoal.get().setTargetdate(LocalDate.parse(goal.getTargetdate(), formatter));
        savingGoalRepository.save(savedgoal.get());

        return SavingGoal_ReturnDTO.builder()
                .savinggoalid(savedgoal.get().getSavinggoalid())
                .amountdifference(goal.getTargetamount()-currentUser.getWallet())
                .targetdate(LocalDate.parse(goal.getTargetdate(), formatter))
                .creationdate(savedgoal.get().getCreationdate())
                .daysremaining(Period.between(LocalDate.now(), LocalDate.parse(goal.getTargetdate(), formatter)))
                .iscompleted(goal.getTargetamount()-currentUser.getWallet() < 0)
                .username(currentUser.getUsername())
                .targetamount(goal.getTargetamount())
                .build();
    }

    public String removeGoal(Integer goalId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findUserEntityByUsername(username).get();
        Optional<SavingGoal> savedgoal = savingGoalRepository.findBySavinggoalid(goalId);
        if(savedgoal.isEmpty()){
            throw new Exception("Goal Not Found");
        }
        if(!savedgoal.get().getUsername().equals(currentUser.getUsername())){
            throw new Exception("Saving Goal is not Your to Edit!");
        }
        savingGoalRepository.delete(savedgoal.get());
        return "Deletion Successful";
    }
}
