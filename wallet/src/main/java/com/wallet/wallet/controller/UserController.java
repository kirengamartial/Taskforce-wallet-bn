package com.wallet.wallet.controller;

import com.wallet.wallet.dto.AuthResponseDTO;
import com.wallet.wallet.dto.ErrorResponse;
import com.wallet.wallet.dto.LoginDTO;
import com.wallet.wallet.dto.RegisterDTO;
import com.wallet.wallet.entity.User;
import com.wallet.wallet.repository.UserRepository;
import com.wallet.wallet.security.JWTGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto){
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Username is Taken", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>("User registered success", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            Optional<User> user = userRepository.findByUsername(loginDto.getUsername());

            return new ResponseEntity<>(
                    new AuthResponseDTO(token, loginDto.getUsername(), user.get().getId()),
                    HttpStatus.OK
            );

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Invalid username or password"),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("An error occurred during login"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}