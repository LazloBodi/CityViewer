package com.lazlob.cityviewer.services;

import com.lazlob.cityviewer.exceptions.UnauthorizedException;
import com.lazlob.cityviewer.models.dtos.TokenResponse;
import com.lazlob.cityviewer.models.entities.Account;
import com.lazlob.cityviewer.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

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

}
