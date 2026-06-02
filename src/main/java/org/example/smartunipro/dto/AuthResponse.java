package org.example.smartunipro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartunipro.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String message;
    private Long userId;
    private String email;
    private String name;
    private Role role;
}
