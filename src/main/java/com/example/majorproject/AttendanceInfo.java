package com.example.majorproject;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;


@Data
@Entity
@Table(name = "attendance_info")
public class AttendanceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@JsonIgnore /////////////////////////It instructs the serialization process to exclude specific fields or properties from being included in the JSON output. This can be useful in avoiding circular references or excluding sensitive information from the JSON output.
//    @ManyToOne(fetch = FetchType.EAGER)     //cascade=CascadeType.ALL
//    @JoinColumn(name = "user_id")
//    private UserInfo userinfo;

    private String status;

    @Column(nullable = false)
    private Date date;   //java.sql.Date stores only date part excluding time part unlike java.util.date which contains both date and time in it

//    @ManyToOne(fetch = FetchType.EAGER)   //cascade=CascadeType.ALL
//    @JoinColumn(name = "bus_id")
//    private BusInfo busInfo;

//    @ManyToOne(fetch = FetchType.EAGER)     //cascade=CascadeType.ALL
//    @JoinColumn(name = "driver_id")
//    private DriverInfo driverInfo;

    @Column(nullable = false)
    private String driverEmail;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private Long busNo;


}

