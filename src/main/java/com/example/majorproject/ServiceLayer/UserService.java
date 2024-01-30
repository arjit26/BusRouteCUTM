package com.example.majorproject.ServiceLayer;

import com.example.majorproject.BusInfo;
import com.example.majorproject.UserInfo;
import com.example.majorproject.DriverInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<UserInfo> getUserInfo(Long id);
    String registerUser(UserInfo userInfo);
    //String loginUser(UserInfo userInfo);
    Object getBusDetailsByUserId(Long userId);
    Object getdriverDetailsByUserId(Long userId);
    String updateUserPassword(Long id, String oldPass, String newPass);

    Object changeLocation(Long id, String newLocation);
    List<BusInfo> getAllBuses();
    Object checkAttendance(String email, String date);

    String generateVerificationToken();

    boolean verifyEmailToken(String token);

    void sendVerificationEmail(String email, String verificationToken);

    //String loginUser(UserInfo userInfo);


}

