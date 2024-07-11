package com.example.demo.pojo.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class AuthRequest {
    
    private String account;
    
    private String password;
    
}
