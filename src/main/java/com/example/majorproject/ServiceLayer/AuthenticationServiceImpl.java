package com.example.majorproject.ServiceLayer;

import com.example.majorproject.*;
import com.example.majorproject.RqstRes.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private UserInfoRepository userInfoRepository;
    private BusInfoRepository busInfoRepository;
    private AdminInfoRepository adminInfoRepository;
    private DriverInfoRepository driverInfoRepository;
    private AttendanceInfoRepository attendanceInfoRepository;

    @Autowired
    public AuthenticationServiceImpl(UserInfoRepository userInfoRepository,BusInfoRepository busInfoRepository,AdminInfoRepository adminInfoRepository,DriverInfoRepository driverInfoRepository,AttendanceInfoRepository attendanceInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.busInfoRepository = busInfoRepository;
        this.adminInfoRepository=adminInfoRepository;
        this.driverInfoRepository=driverInfoRepository;
        this.attendanceInfoRepository=attendanceInfoRepository;
    }
    @Override
    public Object login(LoginCredentials loginInfo) {
        if (loginInfo.getEmail() == "" || loginInfo.getPassword() == "") {
            return "Cannot log in. Please enter both email and password.";
        }

        Optional<AdminInfo> adminOptional = adminInfoRepository.findByEmail(loginInfo.getEmail());
        Optional<DriverInfo> driverOptional = driverInfoRepository.findByEmail(loginInfo.getEmail());
        Optional<UserInfo> userOptional = userInfoRepository.findByEmail(loginInfo.getEmail());
        if (adminOptional.isPresent()) {
            AdminInfo admin = adminOptional.get();
            if (admin.getPassword().equals(loginInfo.getPassword())) {
                // Successful login, return admin information
                //return "Logged in successfully: Your Email is : "+admin.getEmail();
                return admin;

            } else {
                return "Incorrect Email or password";
            }
        }
        else if (driverOptional.isPresent()) {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


            DriverInfo driver = driverOptional.get();

            if (bCryptPasswordEncoder.matches(loginInfo.getPassword(), driver.getPassword())){

            //if (driver.getPassword().equals(loginInfo.getPassword())) {
                // Successful login, return admin information
                return driver;

            } else {
                return "Email or password is incorrect.";
            }
        }
//        else if (userOptional.isPresent()) {
//            UserInfo user = userOptional.get();
//            if (user.getPassword().equals(loginInfo.getPassword())) {
//                // Successful login, return user information
//                return user;
//            } else {
//                return "Email or password is incorrect.";
//            }
//        }

        else if (userOptional.isPresent()) {
            
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            
            UserInfo user = userOptional.get();
            if (user.getEmail()==null || user.getPassword()==null){
                return "Cannot log in. Please enter both email and password.";
            }
            //Optional<UserInfo> userInfoOptional = userInfoRepository.findByEmail(user.getEmail());
//            if (userInfoOptional.isPresent()){
//                UserInfo userInfo = userInfoOptional.get();

            else if (bCryptPasswordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
                //else if (user.getPassword().equals(loginInfo.getPassword())){
                if (user.isEmailVerified()){
                    return "Logged In successfully";
                } else {
                    return "Email is not verified. Please verify your email to login.";
                }
            } else {
                return "Email or Password is not Corrected";
            }
        } else {
            return "You are not registered. First register, then log in here.";
        }
    }
}


/*
if (userInfo.getEmail() == null || userInfo.getPassword() == null) {
        return "Cannot log in. Please enter both email and password.";
        }

        Optional<UserInfo> userOptional = userInfoRepository.findByEmail(userInfo.getEmail());
        if (userOptional.isPresent()) {
        UserInfo user = userOptional.get();
        if (user.getPassword().equals(userInfo.getPassword())) {
        if (user.isEmailVerified()) {
        // Successful login, return user information
        return "Logged in successfully";
        } else {
        return "Email is not verified. Please verify your email to log in.";
        }
        } else {
        return "Email or password is incorrect.";
        }
        } else {
        return "User is not registered. First register, then log in here.";
        }*/

