package com.foundit.service;

import com.foundit.dto.UserDTO;
import com.foundit.model.User;
import java.util.List;

public interface UserService {
    UserDTO create(User user);
    List<UserDTO> getAll();
    UserDTO getById(Long id);
    UserDTO update(Long id, User userDetails);
    void delete(Long id);
    void updateSecurityPhoto(Long id, String photoUrl);
}
