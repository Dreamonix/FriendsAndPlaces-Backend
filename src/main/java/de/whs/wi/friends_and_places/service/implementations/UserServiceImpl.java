package de.whs.wi.friends_and_places.service.implementations;

import de.whs.wi.friends_and_places.controller.dto.UserLoginDTO;
import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.repository.UserRepository;
import de.whs.wi.friends_and_places.service.UserService;
import de.whs.wi.friends_and_places.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User register(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new IllegalArgumentException("UserRegisterDTO must not be null");
        }
        if (userRegisterDTO.getUsername() == null || userRegisterDTO.getEmail() == null ||
            userRegisterDTO.getPassword() == null || userRegisterDTO.getCity() == null ||
            userRegisterDTO.getZipCode() == null || userRegisterDTO.getStreet() == null ||
            userRegisterDTO.getHouseNumber() == null || userRegisterDTO.getMobile() == null) {
            throw new IllegalArgumentException("All fields in UserRegisterDTO must be provided");
        }
        if (existsByUsername(userRegisterDTO.getUsername()) || existsByEmail(userRegisterDTO.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setCity(userRegisterDTO.getCity());
        user.setZipCode(userRegisterDTO.getZipCode());
        user.setStreet(userRegisterDTO.getStreet());
        user.setHouseNumber(userRegisterDTO.getHouseNumber());
        user.setMobile(userRegisterDTO.getMobile());
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    private boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public String authenticate(UserLoginDTO userLoginDTO) {
                Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getEmail(),
                        userLoginDTO.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }
}

