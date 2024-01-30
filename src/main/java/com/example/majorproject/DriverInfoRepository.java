package com.example.majorproject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverInfoRepository extends JpaRepository<DriverInfo,Long> {
    Optional<DriverInfo> findByBusInfo(BusInfo busInfo);
    boolean existsByEmail(String email);

    Optional<DriverInfo> findByEmail(String email);


}
