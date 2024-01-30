package com.example.majorproject;

import jakarta.persistence.*;
import lombok.Data;

@Data //Automatically generates getters, setters, toString, equals, and hashCode for fields.
@Entity  //converts java class to table
@Table(name = "admin_info")  //specify details about the database table
public class AdminInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'admin'")
    private String code;

    // Getters and setters
}

