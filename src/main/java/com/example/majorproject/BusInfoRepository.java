package com.example.majorproject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusInfoRepository extends JpaRepository<BusInfo, Long> {  //string
    Optional<BusInfo> findByLocation( String location);
    Optional<BusInfo> findByBusNo( Long id);
    boolean existsByBusNo(Long busNo);
    boolean existsByDriverEmail(String email);
    boolean existsByLocation(String location);
}
