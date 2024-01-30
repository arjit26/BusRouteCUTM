package com.example.majorproject.ServiceLayer;

import com.example.majorproject.AdminInfo;
import com.example.majorproject.AttendanceInfo;
import com.example.majorproject.DriverInfo;
import com.example.majorproject.UserInfo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    Optional<DriverInfo> getDriverInfo(Long id);
    //String loginDriver(DriverInfo driverInfo);
    String updateDriverPassword(Long id, String oldPass, String newPass);
    //List<UserInfo> getUsersByDriverId(Long driverId);
    Object getUsersByDriverId(Long id);
    Object getRegisteredUsersInDriverBus(Long driverId);
    Object addAttendance(List<AttendanceInfo> attendanceRequests);
}

