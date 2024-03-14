package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.dtos.AuthDTO;
import com.tmploeg.hotelbooker.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController extends ControllerBase {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("register")
  public ResponseEntity<?> register(@RequestBody AuthDTO registerDTO) {
    if (registerDTO == null) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "register data is required");
    }

    if (registerDTO.getUsername() == null) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "username is required");
    }

    if (registerDTO.getUsername().isBlank()
        || userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "username is invalid");
    }

    User newUser = new User(registerDTO.getUsername());
    userRepository.save(newUser);

    return ResponseEntity.ok(newUser);
  }
}
