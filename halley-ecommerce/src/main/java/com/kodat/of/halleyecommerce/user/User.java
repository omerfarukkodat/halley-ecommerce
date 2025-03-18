package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean accountLocked;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private Cart cart;

    private String resetPasswordToken;

    private Instant resetPasswordExpiryDate;


    @Column(nullable = false, updatable = false)
    @CreationTimestamp()
    private LocalDate creationDate;

    @Column(nullable = false)
    @UpdateTimestamp()
    private LocalDate lastUpdateDate;



}