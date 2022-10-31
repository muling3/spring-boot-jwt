package com.example.mulinge.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.mulinge.jwt.utils.JWTConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/refresh")){
            filterChain.doFilter(request, response);
        }else{
            //getting authorization header
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            //check if it starts with bearer
            if(authorization != null && authorization.startsWith("Bearer")){
                //get the token
                String token = authorization.split(" ")[1];
                logger.info("Provided access token: "+ token);

                //verify the token
                try{
                    DecodedJWT decodedJWT = JWTConfig.verifyAccessToken(token);

                    //username
                    String username = decodedJWT.getSubject();
                    logger.info("Decoded user: "+ username);

                    ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException exception){

                    Map<String, String> errResponse = new HashMap<>();
                    errResponse.put("error", exception.getMessage());

                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), errResponse);
                }

            }else{
                filterChain.doFilter(request, response);
            }
        }
    }
}
