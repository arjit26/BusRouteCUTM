package com.example.majorproject;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "bus_info")
//@JsonIgnoreProperties("driverInfo")
//@JsonIgnore
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BusInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private Long busNo;

    @Column(nullable = false,unique = true)
    private String location;

//child
//    @JsonBackReference
//    @OneToOne(mappedBy = "busInfo",fetch = FetchType.EAGER,cascade=CascadeType.ALL)
//    private DriverInfo driverInfo;

    @Column(unique = true)  //nullable=true
    private String driverEmail;

    @Column(columnDefinition = "bigint not null default 0")
    private Long registeredUsers=0L;  //Long

}
