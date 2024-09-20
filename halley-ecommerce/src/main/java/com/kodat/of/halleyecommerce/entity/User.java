package com.kodat.of.halleyecommerce.entity;

import com.kodat.of.halleyecommerce.entity.enums.Role;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private boolean enabled;
    private String phone;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private boolean accountLocked;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp()
    private LocalDate creationDate;
    @Column(nullable = false)
    @UpdateTimestamp()
    private LocalDate lastUpdateDate;



}