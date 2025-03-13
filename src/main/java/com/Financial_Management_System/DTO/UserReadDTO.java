package com.Financial_Management_System.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO {
    private String username;
    private String email;
    private String phone;
    private Double wallet;
    private Set<String> category = new HashSet<>();
}
