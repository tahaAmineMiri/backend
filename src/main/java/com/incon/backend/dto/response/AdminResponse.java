// AdminResponse.java
package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private Integer adminId;
    private String adminName;
    private String adminEmail;
    private String adminRole;
    private LocalDateTime adminCreatedAt;
}