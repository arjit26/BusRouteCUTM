package com.example.majorproject.RqstRes;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String oldPass;
    private String newPass;
}
