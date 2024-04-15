package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.AuthDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @PostMapping("login")
  public String Login(@RequestBody AuthDTO authDTO) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password()));
    } catch (AuthenticationException ex) {
      throw new BadRequestException("username or password incorrect");
    }

    return jwtService.generateTokenForUser(authDTO.username());
  }
}
