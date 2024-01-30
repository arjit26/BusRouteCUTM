package com.example.majorproject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminInfoRepository extends JpaRepository<AdminInfo, Long> {
    boolean existsByEmail(String email);
    Optional<AdminInfo> findByEmail(String email);

}
