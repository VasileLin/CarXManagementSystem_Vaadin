package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    private String phone;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private String address;
    private String email;
    private Boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
