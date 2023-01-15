package net.vorlon.iqfamily.config;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.service.JwtTokenProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenProviderService authencationProvider;

    @Value("${token.secret}")
    private String jwtSecret;

    @Value("${token.expiration.minutes}")
    private int jwtExpiration;

    @Value("${token.prefix}")
    private String prefix;

    @Value("${token.audience}")
    private String audience;

    @Value("${token.type}")
    private String type;

    @Value("${token.issuer}")
    private String issuer;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            username = authencationProvider.getUsernameFromJwtToken(jwt);
            log.info("username: "+username);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            List<GrantedAuthority> authorities = authencationProvider.getAuthorities(jwt);

            log.info(authencationProvider.getAll(jwt).toString());

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }


        chain.doFilter(request, response);
    }
}