package com.example.majorproject.ServiceLayer;

import com.example.majorproject.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DriverServiceImpl implements DriverService{
    private UserInfoRepository userInfoRepository;
    private BusInfoRepository busInfoRepository;
    private AdminInfoRepository adminInfoRepository;
    private DriverInfoRepository driverInfoRepository;
    private AttendanceInfoRepository attendanceInfoRepository;

    @Autowired
    public DriverServiceImpl(UserInfoRepository userInfoRepository,BusInfoRepository busInfoRepository,AdminInfoRepository adminInfoRepository,DriverInfoRepository driverInfoRepository,AttendanceInfoRepository attendanceInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.busInfoRepository = busInfoRepository;
        this.adminInfoRepository=adminInfoRepository;
        this.driverInfoRepository=driverInfoRepository;
        this.attendanceInfoRepository=attendanceInfoRepository;
    }

    @Override
    public Optional<DriverInfo> getDriverInfo(Long id) {
        return driverInfoRepository.findById(id);
    }

//    @Override
//    public String loginDriver(DriverInfo driverInfo) {
//        if (driverInfo.getEmail() == null || driverInfo.getPassword() == null) {
//            return "Cannot log in. Please enter both email and password.";
//        }
//
//        Optional<DriverInfo> driverOptional = driverInfoRepository.findByEmail(driverInfo.getEmail());
//        if (driverOptional.isPresent()) {
//            DriverInfo driver = driverOptional.get();
//            if (driver.getPassword().equals(driverInfo.getPassword())) {
//                // Successful login, return admin information
//                return "Logged in successfully";
//
//            } else {
//                return "Email or password is incorrect.";
//            }
//        } else {
//            return "This Driver is not registered.";
//        }
//    }

    @Override
    public String updateDriverPassword(Long id, String oldPass, String newPass){
        if (oldPass == null || newPass == null) {
            return "Please enter both oldPass and newPass.";
        }
        Optional<DriverInfo> driverOptional = driverInfoRepository.findById(id);
        if (driverOptional.isPresent()) {
            DriverInfo driver = driverOptional.get();
            if (driver.getPassword().equals(oldPass)) {
                driver.setPassword(newPass);
                driverInfoRepository.save(driver);
                return "Password changed successfully.";
            } else {
                return "Incorrect old password.";
            }
        } else {
            return "Invalid Driver ID.";
        }
    }

//    @Override
//    public List<UserInfo> getUsersByDriverId(Long driverId) {
//
//        Optional<DriverInfo> driverOptional = driverInfoRepository.findById(driverId);
//
//        if (driverOptional.isPresent()) {
//            DriverInfo driverInfo = driverOptional.get();
//            List<UserInfo> users = userInfoRepository.findByDriverInfo(driverInfo);
//            return users;
//        } else {
//            return null;
//        }
//    }


    @Override
    public Object getUsersByDriverId(Long driverId) {
        Optional<DriverInfo> driverOptional = driverInfoRepository.findById(driverId);

        if (!driverOptional.isPresent()) {
            // Driver with this ID does not exist
//            return new HashMap<String, String>() {{
//                put("message", "Invalid Id or You are Not Registered");
//            }};

            return  "Invalid Id or You are Not Registered";

        }

        DriverInfo driverInfo = driverOptional.get();
        List<UserInfo> users = userInfoRepository.findByDriverInfo(driverInfo);

        if (users.isEmpty()) {
//            return new HashMap<String, String>() {{
//                put("message", "Either you are not assigned a bus or there are no users registered for your bus");
//            }};

            return "Either you are not assigned a bus or there are no users registered for your bus";

        }

        // If users are found, convert them to the desired format
        List<Map<String, Object>> userResponseList = new ArrayList<>();
        for (UserInfo user : users) {
            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("id",user.getId());
            userResponse.put("name", user.getName());
            userResponse.put("email", user.getEmail());
            userResponse.put("password", user.getPassword());
            userResponse.put("location", user.getLocation());
            userResponse.put("registrationNo", user.getRegistrationNo());
            userResponse.put("busPassNo",user.getBusPassNo());
            userResponse.put("busInfo",user.getBusInfo());
            userResponse.put("driverInfo",user.getDriverInfo());
            userResponse.put("code",user.getCode());

            userResponseList.add(userResponse);
        }

        return userResponseList;
    }

    @Override
    public Object getRegisteredUsersInDriverBus(Long driverId) {
        Optional<DriverInfo> driverOptional = driverInfoRepository.findById(driverId);

        if (!driverOptional.isPresent()) {
            // Driver with this ID does not exist
            return new HashMap<String, String>() {{
                put("message", "Invalid id or you are not registered");
            }};
        }

        DriverInfo driverInfo = driverOptional.get();
        if (driverInfo.getBusInfo() == null) {
            // Driver is not assigned a bus
            return new HashMap<String, String>() {{
                put("message", "Either You are not assigned a bus or there are no users who have registered your bus.");
            }};
        }

        BusInfo busInfo = driverInfo.getBusInfo();
        Long registeredUsers = busInfo.getRegisteredUsers();

        return new HashMap<String, String>() {{
            put("message", "Total registered users in your bus are: " + registeredUsers);
        }};
    }

    @Override
    public Object addAttendance(List<AttendanceInfo> attendanceInfoList) {

//        //convert a String date in the format "yyyy-MM-dd" to a java.sql.Date object.
//        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
//
//        // Convert the String busNo to a Long
//        Long busNoLong = Long.valueOf(busNo);
//        List<AttendanceInfo> attendanceDetails = attendanceInfoRepository.findByDate(sqlDate);

        for (AttendanceInfo attendanceInfo : attendanceInfoList) {
            // Check if attendance record with the same key already exists
            Optional<AttendanceInfo> existingAttendance = attendanceInfoRepository.findByDateAndBusNoAndDriverEmailAndUserEmail(attendanceInfo.getDate(), attendanceInfo.getBusNo(), attendanceInfo.getDriverEmail(), attendanceInfo.getUserEmail());

            if (existingAttendance.isPresent()) {
                // Update the existing record
                AttendanceInfo existingInfo = existingAttendance.get();
                existingInfo.setStatus(attendanceInfo.getStatus());
                attendanceInfoRepository.save(existingInfo);
            } else {
                // Save a new record
                attendanceInfoRepository.save(attendanceInfo);
            }
        }
        return "Attendance added successfully";

    }
}

