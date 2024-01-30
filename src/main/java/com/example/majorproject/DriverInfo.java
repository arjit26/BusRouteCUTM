package com.example.majorproject;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "driver_info")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DriverInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String contactNo;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    //parent
    //@JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER)      //cascade=CascadeType.AL
    @JoinColumn(name = "bus_id")
    //@JsonIdentityReference(alwaysAsId = true)
    private BusInfo busInfo;


    @Column(nullable = false, columnDefinition = "varchar(255) default 'driver'")
    private String code="driver";
}

