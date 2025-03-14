package com.Financial_Management_System.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingGoal_ReturnDTO {

    private String username;
    private Integer savinggoalid;
    private Double targetamount;
    private LocalDate targetdate;
    private Boolean iscompleted;
    private LocalDate creationdate;
    private Period daysremaining;
    private Double amountdifference;

}
