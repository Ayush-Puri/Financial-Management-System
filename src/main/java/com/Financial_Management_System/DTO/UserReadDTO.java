package com.Financial_Management_System.DTO;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO {
    private String username;
    private String email;
    private String phone;
    private Double wallet;
    private Set<String> category;
}
