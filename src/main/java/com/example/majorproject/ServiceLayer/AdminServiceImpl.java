package com.example.majorproject.ServiceLayer;

import com.example.majorproject.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService{
    private UserInfoRepository userInfoRepository;
    private BusInfoRepository busInfoRepository;
    private AdminInfoRepository adminInfoRepository;
    private DriverInfoRepository driverInfoRepository;
    private AttendanceInfoRepository attendanceInfoRepository;

    /*@Autowired
    private PasswordEncoder passwordEncoder;*/

    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${spring.mail.username}") // Inject sender email from application properties
    private String senderEmail;

    @Autowired
    public AdminServiceImpl(UserInfoRepository userInfoRepository,BusInfoRepository busInfoRepository,AdminInfoRepository adminInfoRepository,DriverInfoRepository driverInfoRepository,AttendanceInfoRepository attendanceInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.busInfoRepository = busInfoRepository;
        this.adminInfoRepository=adminInfoRepository;
        this.driverInfoRepository=driverInfoRepository;
        this.attendanceInfoRepository=attendanceInfoRepository;
    }

    @Override
    public Optional<AdminInfo> getAdminInfo() {
        return adminInfoRepository.findByEmail("ruchita1@gmail.com");
    }
//    @Override
//    public Object loginAdmin(AdminInfo adminInfo) {
//        if (adminInfo.getEmail() == null || adminInfo.getPassword() == null) {
//            return "Cannot log in. Please enter both email and password.";
//        }
//
//        Optional<AdminInfo> adminOptional = adminInfoRepository.findByEmail(adminInfo.getEmail());
//        if (adminOptional.isPresent()) {
//            AdminInfo admin = adminOptional.get();
//            if (admin.getPassword().equals(adminInfo.getPassword())) {
//                // Successful login, return admin information
//                //return "Logged in successfully: Your Email is : "+admin.getEmail();
//                return admin;
//
//            } else {
//                return "Incorrect Email or password";
//            }
//        } else {
//            return "This Admin is not registered.";
//        }
//    }

    @Override
    public String updateAdminPassword(Long id, String oldPass, String newPass){
        if (oldPass == null || newPass == null) {
            return "Please enter both oldPass and newPass.";
        }
        Optional<AdminInfo> adminOptional = adminInfoRepository.findById(id);
        if (adminOptional.isPresent()) {
            AdminInfo admin = adminOptional.get();
            if (admin.getPassword().equals(oldPass)) {
                admin.setPassword(newPass);
                adminInfoRepository.save(admin);
                return "Password changed successfully.";
            } else {
                return "Incorrect old password.";
            }
        } else {
            return "Invalid Admin ID.";
        }
    }

    @Override
    public List<UserInfo> findAllUsers() {
        return userInfoRepository.findAll();
    }

    @Override
    public List<DriverInfo> getAllDrivers() {
        return driverInfoRepository.findAll();
    }

    @Override
    public List<BusInfo> getAllBuses() {
        return busInfoRepository.findAll();
    }

    @Override
    public Object getAttendanceDetails(String date, String busNo) {

        //convert a String date in the format "yyyy-MM-dd" to a java.sql.Date object.
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);

        // Convert the String busNo to a Long
        Long busNoLong = Long.valueOf(busNo);

        // Check if attendance records exist for the given date
        List<AttendanceInfo> attendanceDetails = attendanceInfoRepository.findByDate(sqlDate);

        if (attendanceDetails.isEmpty()) {
            return "No attendance is there for this date.";
        }

        // Check if attendance records exist for the given date and busNo
        List<AttendanceInfo> attendanceByDateAndBusNo = attendanceInfoRepository.findByDateAndBusNo(sqlDate, busNoLong);
        if (attendanceByDateAndBusNo.isEmpty()) {
            return "No attendance is there for this bus_no on this date.";
        }

        // Example of finding by date and bus_no (You might need to adjust it according to your data model)
        return attendanceByDateAndBusNo;
    }

    @Override
    public Object addUser(UserInfo userInfo) {
        if (userInfo.getName() == "" || userInfo.getEmail() == "" || userInfo.getPassword() == "" || userInfo.getLocation() == "" || userInfo.getRegistrationNo()=="") {
            return "Cannot add User. Please enter all fields";
        }

        if(driverInfoRepository.existsByEmail(userInfo.getEmail()) || adminInfoRepository.existsByEmail(userInfo.getEmail())){
            return "This user is already registered as driver or admin";
        }

        if (userInfoRepository.existsByEmail(userInfo.getEmail())) {
            return "Cannot add again. User with this Email is already registered.";
        }

        if (userInfo.getPassword().length() < 4) {
            return "Password should be at least 4 characters.";
        }
        if (userInfoRepository.existsByRegistrationNo(userInfo.getRegistrationNo())) {
            return "Invalid registrationNo or some user is already registered with this registrationNo";
        }

        String truncatedBusPassNo = generateShortUUID(); // Generate shorter UUID
        userInfo.setBusPassNo(truncatedBusPassNo);

        Optional<BusInfo> busOptional = busInfoRepository.findByLocation(userInfo.getLocation());
        if(busOptional.isPresent()){
            BusInfo busInfo = busOptional.get();
            userInfo.setBusInfo(busInfo);

            Optional<DriverInfo> driverInfoOptional=driverInfoRepository.findByBusInfo(busInfo);  //checks if this bus is assigned to any driver
            if(driverInfoOptional.isPresent()){
                DriverInfo driverInfo = driverInfoOptional.get();
                userInfo.setDriverInfo(driverInfo);
            }

            Long registeredUsers = busInfo.getRegisteredUsers() + 1;
            busInfo.setRegisteredUsers(registeredUsers);
            busInfoRepository.save(busInfo);
        }
        else{
            return "Sry,this user cannot be registered because for his/her location no bus is available in our campus";
        }

        // Generate verification token and set email verification status
        String verificationToken = generateVerificationToken();
        userInfo.setVerificationToken(verificationToken);
        userInfo.setEmailVerified(false);

        //userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String addUserEncryptPwd = bCryptPasswordEncoder.encode(userInfo.getPassword());
        userInfo.setPassword(addUserEncryptPwd);

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        if (savedUserInfo != null) {
            //return "User added successfully.";
            sendAdminVerificationEmail(savedUserInfo.getEmail(), verificationToken);
            return "User added successfully. Verification email sent.";
        } else {
            return "User registration failed.";
        }
    }


    @Override
    public String generateVerificationToken() {
        // Generate a random token, you can use UUID or any other method
        return UUID.randomUUID().toString();
    }


    @Override
    public void sendAdminVerificationEmail(String email, String verificationToken) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setSubject("Verify Your Email address");

            String verificationLink = "https://localhost:8080/verify-email?token=" + verificationToken;
            String emailContent = "<p>Click the following link to verify your email:</p>"
                    + "<a href=\"" + verificationLink + "\">Verify Email</a>";

            helper.setText(emailContent, true);
            helper.setFrom(senderEmail);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private String generateShortUUID() {
        UUID uuid = UUID.randomUUID();
        String shortUUID = Long.toHexString(uuid.getMostSignificantBits()).toUpperCase().substring(0, 6);
        return  shortUUID;
    }

    @Override
    public Object addDriver(DriverInfo driverInfo) {
        if (driverInfo.getName() == "" || driverInfo.getEmail() == "" || driverInfo.getPassword() == "" || driverInfo.getContactNo() == "") {
            return "Cannot add Driver. Please enter all fields.";
        }
        if(userInfoRepository.existsByEmail(driverInfo.getEmail()) || adminInfoRepository.existsByEmail(driverInfo.getEmail())){
            return "This driver is already registered as user or admin";
        }
        if (driverInfoRepository.existsByEmail(driverInfo.getEmail())) {
            return "Cannot add again. Driver with this Email is already registered.";
        }
        if (driverInfo.getPassword().length() < 4) {
            return "Password should be at least 4 characters.";
        }

        //driverInfo.setPassword(passwordEncoder.encode(driverInfo.getPassword()));

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String driverEncryptPwd = bCryptPasswordEncoder.encode(driverInfo.getPassword());
        driverInfo.setPassword(driverEncryptPwd);

        DriverInfo savedDriverInfo = driverInfoRepository.save(driverInfo);

        if (savedDriverInfo != null) {
            return "Driver added successfully.";
        } else {
            return "Driver registration failed.";
        }
    }

    @Override
    public Object addBus(BusInfo busInfo) {
        if (busInfo.getBusNo() == null || busInfo.getLocation() == ""||busInfo.getDriverEmail()=="") {
            return "Cannot add Bus. Please enter all fields .";
        }
        if (busInfoRepository.existsByBusNo(busInfo.getBusNo())) {
            return "Cannot add again. Bus with this Bus No is already added.";
        }
        if (busInfoRepository.existsByDriverEmail(busInfo.getDriverEmail())==true) {
            return "This driver is already registered for another bus.Please enter another driver email";
        }
        if (busInfoRepository.existsByLocation(busInfo.getLocation())==true) {
            return "Bus with this location is already registered.";
        }
        if (driverInfoRepository.existsByEmail(busInfo.getDriverEmail())==false) {
            return "This driver is not registered.Please enter a valid driver email";
        }
        BusInfo savedBusInfo = busInfoRepository.save(busInfo);
        Optional<DriverInfo>driverInfoOptional=driverInfoRepository.findByEmail(busInfo.getDriverEmail());
        if(driverInfoOptional.isPresent()){
            DriverInfo driverInfo=driverInfoOptional.get();
            driverInfo.setBusInfo(savedBusInfo);
            driverInfoRepository.save(driverInfo);
        }

        if (savedBusInfo != null) {
            return "Bus added successfully.";
        } else {
            return "Bus registration failed.";
        }
    }

    @Override
    public String deleteUserById(Long userId) {
        Optional<UserInfo> userOptional = userInfoRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserInfo userInfo = userOptional.get();
            //userInfoRepository.deleteById(id);
            BusInfo busInfo = userInfo.getBusInfo();

            DriverInfo driverInfo=userInfo.getDriverInfo();

            // Decrement registeredUsers in bus_info
            if (busInfo != null && driverInfo!=null) {
                // Remove the reference to the bus in the user record
                userInfo.setBusInfo(null);
                userInfoRepository.save(userInfo);
                userInfo.setDriverInfo(null);
                userInfoRepository.save(userInfo);

                // Decrease the registered users count for the bus
                Long registeredUsers = busInfo.getRegisteredUsers() - 1;
                busInfo.setRegisteredUsers(registeredUsers);
                busInfoRepository.save(busInfo);
            }
//            List<AttendanceInfo> attendanceRecords = attendanceInfoRepository.findByUserinfo(userInfo);
//            for (AttendanceInfo attendanceRecord : attendanceRecords) {
//                attendanceInfoRepository.delete(attendanceRecord);
//            }
            userInfoRepository.deleteById(userId);
            return "User deleted successfully.";
        } else {
            return "User with this ID is not present.";
        }
    }

    @Override
    public String deleteDriverById(Long driverId) {
        Optional<DriverInfo> driverOptional = driverInfoRepository.findById(driverId);
        if (driverOptional.isPresent()) {
            DriverInfo driverInfo = driverOptional.get();

            // Remove the reference to the bus in the driver record
            BusInfo busInfo = driverInfo.getBusInfo();
            if (busInfo != null) {
                busInfo.setDriverEmail(null);
                driverInfo.setBusInfo(null);
                driverInfoRepository.save(driverInfo);
                busInfoRepository.save(busInfo);

                // Set the driver email in bus_info to null
//                busInfo.setDriverEmail(null);
//                busInfoRepository.save(busInfo);
            }

            List<UserInfo> usersWithDriver = userInfoRepository.findByDriverInfo(driverInfo);
            for (UserInfo user : usersWithDriver) {
                user.setDriverInfo(null);
                userInfoRepository.save(user);
            }

            driverInfoRepository.deleteById(driverId);
            return "Driver deleted successfully.";
        } else {
            return "Driver with this ID is not present.";
        }
    }

    //update driverEmail of a bus by providing bus_id in bus_info table
    @Override
    public String updateBusById(Long id,String email) {
        Optional<BusInfo> busOptional = busInfoRepository.findById(id);
        if (busOptional.isPresent()) {
            BusInfo busInfo = busOptional.get();
            ////if this email is not present in driver info ,if same email is already registered for another bus

            if(busInfoRepository.existsByDriverEmail(email)){
                return "Sry,this driver is already registered for another bus";
            }

            if(!driverInfoRepository.existsByEmail(email)){
                return "Sry,you cannot assign this driver to this bus because this driver is not registered.";
            }
            busInfo.setDriverEmail(email);
            busInfoRepository.save(busInfo);
            Optional<DriverInfo> driverInfoOptional=driverInfoRepository.findByEmail(email);
            if(driverInfoOptional.isPresent()){
                DriverInfo driverInfo=driverInfoOptional.get();
                driverInfo.setBusInfo(busInfo);
                driverInfoRepository.save(driverInfo);
                List<UserInfo> users = userInfoRepository.findByBusInfo(busInfo);
                for (UserInfo user : users) {
                    user.setDriverInfo(driverInfo);
                    userInfoRepository.save(user);
                }
            }
            return "Bus updated successfully.";
        } else {
            return "Bus with this ID is not present.";
        }
    }


    @Override
    public String getUser(Long userId){
        Optional<UserInfo> userOptional = userInfoRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserInfo userInfo = userOptional.get();
            return "User name- " + userInfo.getName()+", User email- "+ userInfo.getEmail()+   "User Location- "+userInfo.getLocation()+ "User's Bus Pass No- "+userInfo.getBusPassNo();
        }
        return "Sorry, User with this id is not present"; // Location not found
    }

    @Override
    public String deleteBusByBusId(Long busId) {
        Optional<BusInfo> busOptional = busInfoRepository.findById(busId);
        if (busOptional.isPresent()) {
            BusInfo bus=busOptional.get();
            Optional<DriverInfo> driverInfoOptional=driverInfoRepository.findByBusInfo(bus);
            if(driverInfoOptional.isPresent()){
                DriverInfo driverInfo=driverInfoOptional.get();
                driverInfo.setBusInfo(null);
            }
            List<UserInfo> userInfo=userInfoRepository.findByBusInfo(bus);
            if(!userInfo.isEmpty()){
                for(UserInfo user:userInfo){
                    user.setDriverInfo(null);
                    user.setBusInfo(null);
                    userInfoRepository.delete(user);

                }
            }
//            if(!userInfo.isEmpty()){
//                userInfoRepository.deleteAll(userInfo);
//                userInfoRepository.saveAll(userInfo);
//            }
            busInfoRepository.delete(bus);
            return "Bus deleted successfully.";
        } else {
            return "Sorry, cannot delete, bus with this busId is not present";
        }
    }
}

