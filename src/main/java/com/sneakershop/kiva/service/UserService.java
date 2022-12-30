package com.sneakershop.kiva.service;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.sneakershop.kiva.entity.User;
import com.sneakershop.kiva.model.dto.UserDTO;
import com.sneakershop.kiva.model.request.ChangePasswordRequest;
import com.sneakershop.kiva.model.request.CreateUserRequest;
import com.sneakershop.kiva.model.request.UpdateProfileRequest;

import java.util.List;

@Service
public interface UserService {
    List<UserDTO> getListUsers();

    Page<User> adminListUserPages(String fullName, String phone, String email, Integer page);

    User createUser(CreateUserRequest createUserRequest);

    void changePassword(User user, ChangePasswordRequest changePasswordRequest);

    User updateProfile(User user, UpdateProfileRequest updateProfileRequest);
}
