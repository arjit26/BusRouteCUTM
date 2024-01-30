package com.example.majorproject;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column( nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, unique = true)
    private String busPassNo;

    @Column(unique = true,nullable = false)
    private String registrationNo;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'user'")
    private String code="user";
    // Getters and setters

    @ManyToOne(fetch = FetchType.EAGER)        //cascade=CascadeType.AL
    @JoinColumn(name = "bus_id")
    private BusInfo busInfo;

    @ManyToOne(fetch = FetchType.EAGER)         //cascade=CascadeType.AL
    @JoinColumn(name = "driver_id")
    private DriverInfo driverInfo;

    @Column(name = "verification_token")
    private String verificationToken;

//    @Column(nullable = false)
//    private boolean active;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;


    // Constructors and other methods
}
