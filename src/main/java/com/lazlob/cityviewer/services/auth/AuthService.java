package com.lazlob.cityviewer.services.auth;

import com.lazlob.cityviewer.exceptions.UnauthorizedException;
import com.lazlob.cityviewer.models.dtos.TokenResponse;
import com.lazlob.cityviewer.models.entities.Account;
import com.lazlob.cityviewer.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements UserDetailsService {

    AccountRepository accountRepository;
    PasswordEncoder encoder;
    JwtTokenService jwtTokenService;

    public TokenResponse login(String username, String password) {
        Optional<Account> account = accountRepository.findById(username);
        if (account.isEmpty() || !encoder.matches(password, account.get().getPassword())) {
            throw new UnauthorizedException();
        }
        return new TokenResponse(jwtTokenService.generateToken(account.get()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findById(username).orElseThrow(UnauthorizedException::new);
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRoles().split(","))
                .build();
    }
}
