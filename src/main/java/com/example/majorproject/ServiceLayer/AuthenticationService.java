package com.example.majorproject.ServiceLayer;

import com.example.majorproject.AdminInfo;
import com.example.majorproject.RqstRes.LoginCredentials;
public interface AuthenticationService {

    Object login(LoginCredentials loginInfo);
}
