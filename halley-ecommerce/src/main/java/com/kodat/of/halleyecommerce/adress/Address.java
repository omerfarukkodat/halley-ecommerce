package com.kodat.of.halleyecommerce.adress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodat.of.halleyecommerce.adress.enums.AddressType;
import com.kodat.of.halleyecommerce.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 20, message = "City name must not exceed 100 characters")
    private String city;

    @NotBlank(message = "District cannot be blank")
    @Size(max = 20, message = "District name must not exceed 100 characters")
    private String district;

    @NotBlank(message = "Neighborhood cannot be blank")
    @Size(max = 100, message = "Neighborhood name must not exceed 100 characters")
    private String neighborhood;

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 100, message = "Street name must not exceed 100 characters")
    private String street;

    @NotBlank(message = "Zip Code cannot be blank")
    @Pattern(regexp = "\\d{5}", message = "Zip Code must be exactly 5 digits")
    private String zipCode;

    private String buildingNumber;
    private String floor;

    @Size(max = 10, message = "Apartment number must not exceed 10 characters")
    private String apartmentNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , name = "address_type")
    private AddressType addressType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
