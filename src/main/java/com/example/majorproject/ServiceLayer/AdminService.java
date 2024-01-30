package com.example.majorproject.ServiceLayer;

import com.example.majorproject.*;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Optional<AdminInfo> getAdminInfo();
    //Object loginAdmin(AdminInfo adminInfo);
    String updateAdminPassword(Long id, String oldPass, String newPass);
    List<UserInfo> findAllUsers();
    List<DriverInfo> getAllDrivers();
    List<BusInfo> getAllBuses();
    //List<AttendanceInfo> getAttendanceDetails(String date, String busNo);
    Object getAttendanceDetails(String date, String busNo);
    Object addUser(UserInfo userInfo);
    Object addDriver(DriverInfo driverInfo);
    Object addBus(BusInfo busInfo);
    String deleteUserById(Long userId);
    String deleteDriverById(Long driverId);
    String updateBusById(Long id,String email);

    String getUser(Long userId);

    String deleteBusByBusId(Long busId);


    String generateVerificationToken();

    void sendAdminVerificationEmail(String email, String verificationToken);
}

