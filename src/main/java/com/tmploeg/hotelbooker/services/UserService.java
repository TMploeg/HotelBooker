package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.Role;
import com.tmploeg.hotelbooker.models.User;
import java.security.Principal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;
  private final BookingRepository bookingRepository;

  public User save(String username, String password, Role role) {
    return userRepository.save(new User(username, passwordEncoder.encode(password), role));
  }

  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("user '" + username + "' not found"));
  }

  public boolean userExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  public Set<User> findByRole(Role role) {
    return userRepository.findByRole(role);
  }

  public Set<Booking> getBookingsForUser(User user) {
    boolean isAdmin = user.getRole() == roleService.findByName(RoleName.ADMIN);

    return isAdmin
        ? bookingRepository.findByOrderByCheckIn()
        : bookingRepository.findByUserOrderByCheckIn(user);
  }

  public User getFromPrincipal(Principal principal) {
    return loadUserByUsername(principal.getName());
  }
}
