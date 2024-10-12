package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import com.kodat.of.halleyecommerce.mapper.user.UserMapper;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleValidator roleValidator;
    public UserServiceImpl(UserRepository userRepository, RoleValidator roleValidator) {
        this.userRepository = userRepository;
        this.roleValidator = roleValidator;
    }

    @Override
    public UserProfileDto getProfile(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
        return UserMapper.toUserProfileDto(user);
    }

    @Override
    public UserProfileDto updateProfile(UserProfileDto userProfileDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
        User updatedUser = UserMapper.updateUserFromDto(user,userProfileDto);
        userRepository.save(updatedUser);
        return UserMapper.toUserProfileDto(updatedUser);
    }


}
