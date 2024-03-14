package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.dtos.AuthDTO;
import com.tmploeg.hotelbooker.dtos.UserDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController extends ControllerBase {
  private final UserRepository userRepository;

  @PostMapping("register")
  public ResponseEntity<UserDTO> register(@RequestBody AuthDTO registerDTO) {
    if (registerDTO == null) {
      throw new BadRequestException("register data is required");
    }

    if (registerDTO.getUsername() == null) {
      throw new BadRequestException("username is required");
    }

    if (registerDTO.getUsername().isBlank()
        || userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
      throw new BadRequestException("username is invalid");
    }

    User newUser = new User(registerDTO.getUsername());
    userRepository.save(newUser);

    return ResponseEntity.ok(UserDTO.fromUser(newUser));
  }
}
