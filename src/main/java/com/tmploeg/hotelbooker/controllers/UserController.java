package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.dtos.AuthDTO;
import com.tmploeg.hotelbooker.dtos.UserDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController extends ControllerBase {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

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

    if (registerDTO.getPassword() == null) {
      throw new BadRequestException("password is required");
    }

    if (registerDTO.getPassword().isBlank()) {
      throw new BadRequestException("password is invalid");
    }

    User newUser =
        new User(registerDTO.getUsername(), passwordEncoder.encode(registerDTO.getPassword()));
    userRepository.save(newUser);

    return ResponseEntity.ok(UserDTO.fromUser(newUser));
  }
}
