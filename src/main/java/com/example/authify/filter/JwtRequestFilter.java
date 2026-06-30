package com.example.authify.filter;

import com.example.authify.service.AppUserDetailService;
import com.example.authify.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_URLS = List.of("/login", "/register", "/send-reset-otp", "/reset-password", "/logout");
    private final AppUserDetailService appUserDetailService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("Path = " + request.getServletPath());
//        System.out.println("Request URI = " + request.getRequestURI());
        String path = request.getServletPath();
        if (PUBLIC_URLS.contains(path)) {
//            System.out.println("PUBLIC URL - SKIPPING JWT FILTER");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = null;
        String email = null;

        // check authorization header
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }

        // if not found on header check in cookies
        if (jwt == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // validate token and set security context
        if (jwt != null) {
            email = jwtUtil.extractEmail(jwt);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = appUserDetailService.loadUserByUsername(email);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
