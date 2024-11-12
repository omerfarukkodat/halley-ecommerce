package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.address.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "guest_users")
public class GuestUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;
    @OneToMany(mappedBy = "guestUser" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> address;

}
