package com.kodat.of.halleyecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phone;
    private LocalDate birthDate;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp()
    private LocalDate creationDate;
    @Column(nullable = false)
    @UpdateTimestamp()
    private LocalDate lastUpdateDate;



}