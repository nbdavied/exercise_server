package com.dw.exercise.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dw.exercise.security.SecurityConstants.*;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        AppAuthToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private AppAuthToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        token = token.replace(TOKEN_PREFIX, "");
        if(token != null){
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token);
            String username = jwt.getSubject();
            Integer userId = jwt.getClaim("uid").asInt();

            if(username != null){
                JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
                return new AppAuthToken(username, null, userId, user.getAuthorities());
            }
            return null;
        }
        return null;
    }
}
