package com.modsensoftware.book_service.config;

import com.modsensoftware.book_service.services.JwtService;
import com.modsensoftware.book_service.utils.HttpRequestUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if(Arrays.stream(SecurityConfiguration.WHITE_LIST_URL).anyMatch(url -> request.getServletPath().contains(url))){
            filterChain.doFilter(request, response);
            return;
        }
        String jwtAccessToken = HttpRequestUtils.extractAccessToken(request);
        if(jwtAccessToken == null){
            filterChain.doFilter(request, response);
            return;
        }

        Claims accessTokenClaims = jwtService.extractAllClaims(jwtAccessToken);
        final String username = jwtService.extractSubject(accessTokenClaims);
        Collection<? extends GrantedAuthority> authorities = jwtService.extractAuthorities(accessTokenClaims);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}