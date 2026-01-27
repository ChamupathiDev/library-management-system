package com.lib.api.dto;

import com.lib.api.model.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String email;
    private Role role;
}
