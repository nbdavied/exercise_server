package com.dw.exercise.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
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
    @Autowired
    private UserDAO userDAO;

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
            DecodedJWT jwt = null;
            try{
                jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token);
            }catch (JWTVerificationException exception){
                return null;
            }
            Integer userId = Integer.parseInt(jwt.getSubject());

            if(userId != null){
                User user = userDAO.getUserById(userId);
                JwtUser jwtUser = JwtUserFactory.create(user, new UserAuth());
                return new AppAuthToken(user.getUsername(), null, userId, jwtUser.getAuthorities());
            }
            return null;
        }
        return null;
    }
}
