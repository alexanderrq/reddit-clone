package com.aramirez.redditclone.service;

import com.aramirez.redditclone.dto.AuthenticationResponse;
import com.aramirez.redditclone.dto.LoginRequest;
import com.aramirez.redditclone.dto.RegisterRequest;
import com.aramirez.redditclone.exception.RedditException;
import com.aramirez.redditclone.model.NotificationEmail;
import com.aramirez.redditclone.model.User;
import com.aramirez.redditclone.model.VerificationToken;
import com.aramirez.redditclone.repository.UserRepository;
import com.aramirez.redditclone.repository.VerificationTokenRepository;
import com.aramirez.redditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.aramirez.redditclone.util.Constants.ACTIVATION_EMAIL;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        log.info("User registered successfully, sending authentication email");

        String token = generateVerificationToken(user);

        String message = mailContentBuilder.build("Thank you for signing up to Reddit," +
                " please click on the below URL to activate your account: "
                + ACTIVATION_EMAIL + "/" + token);

        mailService.sendMail(new NotificationEmail("Please activate your account",
                user.getEmail(), message));
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RedditException("User not found with id - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new RedditException("Invalid token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authenticationToken = jwtProvider.generateToken(authentication);
        return new AuthenticationResponse(authenticationToken, loginRequest.getUsername());
    }
}
