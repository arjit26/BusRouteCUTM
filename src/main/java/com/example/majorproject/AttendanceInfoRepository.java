package com.example.majorproject;

import lombok.extern.flogger.Flogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface AttendanceInfoRepository extends JpaRepository<AttendanceInfo,Long> {

    List<AttendanceInfo> findByDateAndBusNo(java.sql.Date  date, Long busNo);
    //List<AttendanceInfo> findByUserinfo(UserInfo userInfo);
    List<AttendanceInfo>findByDate(java.sql.Date sqlDate);

    Optional<AttendanceInfo> findByDateAndBusNoAndDriverEmailAndUserEmail(java.sql.Date  date, Long busNo, String driverEmail, String userEmail);

    AttendanceInfo findByUserEmailAndDate(String userEmail, java.sql.Date date);

}
