package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.dtos.AuthDTO;
import com.tmploeg.hotelbooker.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController extends ControllerBase {
  private UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("register")
  public ResponseEntity<?> register(@RequestBody AuthDTO registerDTO) {
    if (registerDTO == null) {
      return getBadRequestResponse("register data is required");
    }

    if (registerDTO.getUsername() == null) {
      return getBadRequestResponse("username is required");
    }

    if (registerDTO.getUsername().isBlank()
        || userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
      return getBadRequestResponse("username is invalid");
    }

    User newUser = new User(registerDTO.getUsername());
    userRepository.save(newUser);

    return ResponseEntity.ok(newUser);
  }
}
