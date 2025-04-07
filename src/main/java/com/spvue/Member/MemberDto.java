package com.spvue.Member;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto{
    private Long id;
    private String username;
    private String displayName;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;
    private String profileImage;
    private String country;
    private String mainAddress;
    private String subAddress;

}