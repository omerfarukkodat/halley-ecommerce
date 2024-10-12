package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.adress.Adress;
import com.kodat.of.halleyecommerce.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private boolean enabled;
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private boolean accountLocked;
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Adress> adresses = new ArrayList<>();


    @Column(nullable = false, updatable = false)
    @CreationTimestamp()
    private LocalDate creationDate;
    @Column(nullable = false)
    @UpdateTimestamp()
    private LocalDate lastUpdateDate;



}