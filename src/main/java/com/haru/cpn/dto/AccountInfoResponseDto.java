package com.haru.cpn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountInfoResponseDto {
    private String bankName;
    private String accountNumber;
    private String accountHolder;
}
