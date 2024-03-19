package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.AuthDTO;
import com.tmploeg.hotelbooker.dtos.UserDTO;
import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.models.entities.User;
import com.tmploeg.hotelbooker.services.RoleService;
import com.tmploeg.hotelbooker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ControllerRoutes.USERS)
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final RoleService roleService;

  @PostMapping("register")
  public UserDTO register(@RequestBody AuthDTO registerDTO) {
    if (registerDTO == null) {
      throw new BadRequestException("register data is required");
    }

    if (registerDTO.getUsername() == null) {
      throw new BadRequestException("username is required");
    }

    if (registerDTO.getUsername().isBlank() || userService.userExists(registerDTO.getUsername())) {
      throw new BadRequestException("username is invalid");
    }

    if (registerDTO.getPassword() == null) {
      throw new BadRequestException("password is required");
    }

    if (registerDTO.getPassword().isBlank()) {
      throw new BadRequestException("password is invalid");
    }

    User newUser =
        userService.save(
            registerDTO.getUsername(),
            registerDTO.getPassword(),
            roleService.findByName(RoleName.USER));

    return UserDTO.fromUser(newUser);
  }
}
