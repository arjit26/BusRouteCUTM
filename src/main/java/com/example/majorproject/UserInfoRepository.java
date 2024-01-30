package com.example.majorproject;

import org.hibernate.annotations.SQLSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByEmail(String email);
    boolean existsByRegistrationNo(String registrationNo);
    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findById(Long id);
    List<UserInfo> findByDriverInfo(DriverInfo driverInfo);
    List<UserInfo> findByBusInfo(BusInfo busInfo);

    @Query(value = "select * from user_info where verification_token = :verificationToken", nativeQuery = true)
    Optional<UserInfo> findByVerificationToken(@Param("verificationToken") String verificationToken);
}
