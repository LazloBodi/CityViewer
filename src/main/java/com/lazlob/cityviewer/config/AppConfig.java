package com.lazlob.cityviewer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
@OpenAPIDefinition(info = @Info(title = "CityViewer API", version = "1.0", description = "City information"))
@SecurityScheme(name = AppConfig.SWAGGER_SECURITY_SCHEME_NAME, paramName = HttpHeaders.AUTHORIZATION, scheme = "Bearer", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
public class AppConfig {

    public static final String SWAGGER_SECURITY_SCHEME_NAME = "apikey";

    private static final int BCRYPT_STRENGTH = 10;

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }

}
