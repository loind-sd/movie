package com.cinema.security.audit;

import com.cinema.security.support.JwtClaimExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("springSecurityAuditorAware")
@RequiredArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<Long> {

    private final JwtClaimExtractor extractor;

    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        System.out.println("auth = " + auth);

        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                extractor.extractUserId(jwtAuth.getToken())
        );
    }
}
