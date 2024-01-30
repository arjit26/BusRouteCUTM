package com.example.majorproject.ControllerLayer;

import com.example.majorproject.*;
import com.example.majorproject.RqstRes.PasswordUpdateRequest;
import com.example.majorproject.ServiceLayer.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/getAdmin")
    public ResponseEntity<AdminInfo> getAdmin() {
        Optional<AdminInfo> adminOptional = adminService.getAdminInfo();
        if (adminOptional.isPresent()) {
            return new ResponseEntity<>(adminOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<Object> loginAdmin(@RequestBody AdminInfo adminInfo) {
//        Object loginStatus = adminService.loginAdmin(adminInfo);
//        if (loginStatus instanceof AdminInfo) {
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        }else if(loginStatus instanceof String){
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        }
//        else {
//            return new ResponseEntity<>(loginStatus, HttpStatus.NOT_FOUND);
//        }
//    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateAdminPassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        String oldPass = passwordUpdateRequest.getOldPass();
        String newPass = passwordUpdateRequest.getNewPass();
        String updateStatus = adminService.updateAdminPassword(id, oldPass, newPass);
        if (updateStatus.equals("Password changed successfully.")) {
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateStatus, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findUsers")
    public ResponseEntity<List<UserInfo>> findAllUsers() {
        List<UserInfo> users = adminService.findAllUsers();
        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findDrivers")
    public ResponseEntity<List<DriverInfo>> findDrivers() {
        List<DriverInfo> drivers = adminService.getAllDrivers();
        if (!drivers.isEmpty()) {
            return new ResponseEntity<>(drivers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findBuses")
    public ResponseEntity<List<BusInfo>> getAllBuses() {
        List<BusInfo> buses = adminService.getAllBuses();
        if (!buses.isEmpty()) {
            return new ResponseEntity<>(buses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/attendanceDetails")
//    public ResponseEntity<List<AttendanceInfo>> getAttendanceDetails(
//            @RequestBody Map<String, String> requestBody
//    ) {
//        String date = requestBody.get("date");
//        String busNo = requestBody.get("busNo");
//        if (date == null || busNo == null) {
//            return ResponseEntity.badRequest().body(null); // Return a bad request if either date or busNo is missing
//        }
//
//            List<AttendanceInfo> attendanceDetails = adminService.getAttendanceDetails(date, busNo);
//            if (!attendanceDetails.isEmpty()) {
//                return new ResponseEntity<>(attendanceDetails, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//    }

    @PostMapping("/attendanceDetails")
    public ResponseEntity<Object> getAttendanceDetails(@RequestBody Map<String, String> requestBody) {
        String date = requestBody.get("date");
        String busNo = requestBody.get("busNo");
        if (date == null || busNo == null) {
            return ResponseEntity.badRequest().body("Both date and busNo are required."); // Return a bad request if either date or busNo is missing
        }

        Object result = adminService.getAttendanceDetails(date, busNo);

        if (result instanceof List) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else if (result instanceof String) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<Object> addUser(@RequestBody UserInfo userInfo) {
        Object addStatus = adminService.addUser(userInfo);
//        if (addStatus.equals("Success")) {
//            return new ResponseEntity<>("User added successfully.", HttpStatus.CREATED);
//        }
        if (addStatus instanceof String) {
            return new ResponseEntity<>(addStatus, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(addStatus, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addDriver")
    public ResponseEntity<Object> addDriver(@RequestBody DriverInfo driverInfo) {
        Object addStatus = adminService.addDriver(driverInfo);
//        if (addStatus.equals("Success")) {
//            return new ResponseEntity<>("Driver added successfully. " + "His/her Credentials are " + "Name= " + driverInfo.getName() + ", " + "Email= " + driverInfo.getEmail() + ", " + "Contact No=" + driverInfo.getContactNo(), HttpStatus.CREATED);
//        }
        if (addStatus instanceof String) {
            return new ResponseEntity<>(addStatus, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(addStatus, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addBus")
    public ResponseEntity<Object> addBus(@RequestBody BusInfo busInfo) {
        Object addStatus = adminService.addBus(busInfo);
//        if (addStatus.equals("Success")) {
//            return new ResponseEntity<>("Bus added successfully. " + "Credentials are " + "Bus No= " + busInfo.getBusNo() + ", " + "Location= " + busInfo.getLocation() + ", " + "Driver Email=" + busInfo.getDriverEmail(), HttpStatus.CREATED);
//        }
        if (addStatus instanceof String) {
            return new ResponseEntity<>(addStatus, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(addStatus, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{userId}")       //requestBody
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        String deleteStatus = adminService.deleteUserById(userId);
        if (deleteStatus.equals("User deleted successfully.")) {
            return new ResponseEntity<>(deleteStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(deleteStatus, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteDriver/{driverId}")
    public ResponseEntity<String> deleteDriverById(@PathVariable Long driverId) {
        String deleteStatus = adminService.deleteDriverById(driverId);
        if (deleteStatus.equals("Driver deleted successfully.")) {
            return new ResponseEntity<>(deleteStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(deleteStatus, HttpStatus.NOT_FOUND);
        }
    }

    //update driver_email of a bus providing bus_id in bus_info table
    @PutMapping("/updateBus/{id}")
    public ResponseEntity<String> updateBusById(@PathVariable Long id, @RequestBody Map<String, String> email1) {
        String email = email1.get("email");
        String updateStatus = adminService.updateBusById(id, email);
        if (updateStatus.equals("Bus updated successfully.")) {
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateStatus, HttpStatus.NOT_FOUND);
        }
    }

    //delete a particular assigned driver of a bus//or delete a driver_email of a bus by providing bus_id in bus_info table

    @GetMapping("/findUsers/{userId}")
    public ResponseEntity<String> getUser(@PathVariable Long userId) {
        String response = adminService.getUser(userId);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/deleteBus/{busId}")
    public ResponseEntity<String> deleteBusByBusNo(@PathVariable Long busId) {
        String deleteStatus = adminService.deleteBusByBusId(busId);
        if (deleteStatus.equals("Bus deleted successfully.")) {
            return new ResponseEntity<>(deleteStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(deleteStatus, HttpStatus.NOT_FOUND);
        }
    }



}

