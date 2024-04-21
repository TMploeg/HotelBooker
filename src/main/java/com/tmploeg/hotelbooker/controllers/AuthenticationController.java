package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.AuthDTO;
import com.tmploeg.hotelbooker.dtos.UserDTO;
import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.models.entities.User;
import com.tmploeg.hotelbooker.services.JwtService;
import com.tmploeg.hotelbooker.services.RoleService;
import com.tmploeg.hotelbooker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(ControllerRoutes.AUTH)
@RequiredArgsConstructor
public class AuthenticationController {
  private final JwtService jwtService;
  private final UserService userService;
  private final RoleService roleService;

  @PostMapping("login")
  public String login(@RequestBody AuthDTO authDTO) {
    if (authDTO.username() == null || authDTO.password() == null) {
      throw new BadRequestException("username and password are required");
    }

    if (!userService.userExists(authDTO.username())
        || !userService.isCorrectUserPassword(authDTO.username(), authDTO.password())) {
      throw new BadRequestException("username or password incorrect");
    }

    return jwtService.generateTokenForUser(authDTO.username());
  }

  @PostMapping("register")
  public UserDTO register(@RequestBody @NotNull AuthDTO authDTO, UriComponentsBuilder ucb) {
    if (authDTO.username() == null || authDTO.password() == null) {
      throw new BadRequestException("username and password are required");
    }

    if (userService.userExists(authDTO.username())
        || !userService.isValidPassword(authDTO.password())) {
      throw new BadRequestException("username or password is invalid");
    }

    User user =
        userService.save(
            authDTO.username(), authDTO.password(), roleService.findByName(RoleName.USER));

    return UserDTO.fromUser(user);
  }
}
