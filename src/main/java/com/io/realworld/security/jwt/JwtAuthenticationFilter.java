package com.io.realworld.security.jwt;

import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";

    private final JwtService jwtService;

    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("hello jwt filter");
        Optional<String> token = getToken(request.getHeader(HEADER));
        String email = null;
        String jwt = null;
        if(token.isPresent()){
            jwt = String.valueOf(token);
            email = jwtService.getEmail(jwt);
        }
        if(email != null){
            User findUser = userService.findByEmail(email);

            if(jwtService.validateToken(jwt,findUser)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(findUser,null, AuthorityUtils.NO_AUTHORITIES);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request,response);

    }

    private Optional<String> getToken(String header) {
        if(header == null){
            return Optional.empty();
        }else{
            String[] s = header.split(" ");
            if(s.length < 2){
                return Optional.empty();
            }else{
                return Optional.ofNullable(s[1]);
            }
        }
    }
}