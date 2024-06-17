package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.global.dto.ApiMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res); // go to 'JwtAuthenticationFilter'
        }  catch (SecurityException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
        }
        // 모든 예외에서 내가 원하는 예외만 필터링하게
//        catch (JwtException ex) {
//            setErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
//        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse res, Throwable ex) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json; charset=UTF-8");

        ApiMessageDto apiMessageDto = new ApiMessageDto(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        String json = objectMapper.writeValueAsString(apiMessageDto);
        res.getWriter().write(json);
    }
}
