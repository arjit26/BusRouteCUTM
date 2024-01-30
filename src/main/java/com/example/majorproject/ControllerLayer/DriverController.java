package com.example.majorproject.ControllerLayer;

import com.example.majorproject.AdminInfo;
import com.example.majorproject.AttendanceInfo;
import com.example.majorproject.DriverInfo;
import com.example.majorproject.RqstRes.PasswordUpdateRequest;
import com.example.majorproject.ServiceLayer.AdminService;
import com.example.majorproject.ServiceLayer.DriverService;
import com.example.majorproject.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/driver")
public class DriverController {
    private DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }


    @GetMapping("/getDriver/{id}")
    public ResponseEntity<DriverInfo> getDriver(@PathVariable Long id) {
        Optional<DriverInfo> driverOptional = driverService.getDriverInfo(id);
        if (driverOptional.isPresent()) {
            return new ResponseEntity<>(driverOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginDriver(@RequestBody DriverInfo driverInfo) {
//        String loginStatus = driverService.loginDriver(driverInfo);
//        if (loginStatus.equals("Logged in successfully")) {
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(loginStatus, HttpStatus.NOT_FOUND);
//        }
//    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateDriverPassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        String oldPass = passwordUpdateRequest.getOldPass();
        String newPass = passwordUpdateRequest.getNewPass();
        String updateStatus = driverService.updateDriverPassword(id, oldPass, newPass);
        if (updateStatus.equals("Password changed successfully.")) {
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateStatus, HttpStatus.BAD_REQUEST);
        }
    }

    //get all the users in the driverbus
//    @GetMapping("/getUsersByDriverId/{driverId}")
//    public ResponseEntity<List<UserInfo>> getUsersByDriverId(@PathVariable Long driverId) {
//        List<UserInfo> users = driverService.getUsersByDriverId(driverId);
//        if (users != null) {
//            return new ResponseEntity<>(users, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/getUsersByDriver/{id}")
    public ResponseEntity<Object> getUsersByDriverId(@PathVariable Long id) {
        Object result = driverService.getUsersByDriverId(id);

        if (result instanceof Object) {
            // Users found, return the list
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else if (result instanceof String) {
            // Check if it's a message, then return it with the appropriate HTTP status
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
//        else if (result instanceof Map) {
//            // Check if it's a message, then return it with the appropriate HTTP status
//            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//        }

        // Handle other cases if needed
        else{
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getRegisteredUsersInDriverBus/{driverId}")
    public ResponseEntity<Object> getRegisteredUsersInDriverBus(@PathVariable Long driverId) {
        Object result = driverService.getRegisteredUsersInDriverBus(driverId);
        if (result instanceof Map) {
            // Check if it's a message, then return it with the appropriate HTTP status
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // Handle other cases if needed
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addAttendance")
    public ResponseEntity<Object> addAttendance(@RequestBody List<AttendanceInfo> attendanceInfoList) {
        Object result = driverService.addAttendance(attendanceInfoList);
        if (result instanceof String) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}

