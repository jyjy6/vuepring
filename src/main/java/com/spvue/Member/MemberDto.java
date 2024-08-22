package com.spvue.Member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDto{
    private String username;
    private String displayName;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;



    MemberDto(String username, String displayName, String email,
              String phone, LocalDateTime createdAt, LocalDateTime updatedAt, String role ){
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;

    }


}