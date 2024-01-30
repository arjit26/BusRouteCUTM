package com.example.majorproject;

import org.hibernate.annotations.SQLSelect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByEmail(String email);
    boolean existsByRegistrationNo(String registrationNo);
    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findById(Long id);
    List<UserInfo> findByDriverInfo(DriverInfo driverInfo);
    List<UserInfo> findByBusInfo(BusInfo busInfo);


    Optional<UserInfo> findByVerificationToken(String verificationToken);
}
