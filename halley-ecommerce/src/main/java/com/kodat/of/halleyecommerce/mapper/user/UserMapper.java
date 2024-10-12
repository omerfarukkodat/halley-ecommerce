package com.kodat.of.halleyecommerce.mapper.user;

import com.kodat.of.halleyecommerce.adress.Address;
import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import com.kodat.of.halleyecommerce.user.User;

import java.util.List;


public class UserMapper {

    public static UserProfileDto toUserProfileDto(User user){
        return UserProfileDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .addresses(user.getAdresses())
                .build();
    }
    public static User updateUserFromDto(User existingUser , UserProfileDto userProfileDto){
        existingUser.setFirstName(userProfileDto.getFirstName());
        existingUser.setLastName(userProfileDto.getLastName());
        existingUser.setEmail(userProfileDto.getEmail());
        existingUser.setPhone(userProfileDto.getPhone());

        List<Address> updatedAdresses = userProfileDto.getAddresses();
        existingUser.getAdresses().clear();
        for (Address addresses: updatedAdresses){
            addresses.setUser(existingUser);
            existingUser.getAdresses().add(addresses);
        }
        return existingUser;
    }
}
