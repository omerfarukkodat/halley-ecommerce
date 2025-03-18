package com.kodat.of.halleyecommerce.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodat.of.halleyecommerce.address.enums.AddressType;
import com.kodat.of.halleyecommerce.user.GuestUser;
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
    @Size(max = 300, message = "General address must not exceed 300 characters")
    private String generalAddress;

    @NotBlank(message = "Zip Code cannot be blank")
    @Pattern(regexp = "\\d{5}", message = "Zip Code must be exactly 5 digits")
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , name = "address_type")
    private AddressType addressType;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private GuestUser guestUser;

    private Boolean isDeleted;



    @Override
    public String toString() {
        return
                "şehir='" + city + '\'' +
                ", ilçe='" + district + '\'' +
                ", mahalle='" + neighborhood + '\'' +
                ", genel adres='" + generalAddress + '\'' +
                ", posta kodu='" + zipCode + '\'' +
                ", adres tipi=" + addressType +
                '}';
    }
}
