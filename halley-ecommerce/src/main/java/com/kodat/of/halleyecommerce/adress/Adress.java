package com.kodat.of.halleyecommerce.adress;

import com.kodat.of.halleyecommerce.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "adresses")
public class Adress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String district;
    private String neighborhood;
    private String street;
    private String zipCode;
    private String buildingNumber;
    private String floor;
    private String apartmentNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
