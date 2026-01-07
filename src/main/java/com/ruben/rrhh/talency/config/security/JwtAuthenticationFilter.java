package com.ruben.rrhh.talency.config.security;

import com.ruben.rrhh.talency.service.CustomUserDetails;
import com.ruben.rrhh.talency.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            username = jwtUtil.extractEmail(jwt);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("TOKEN EXPIRADO");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("TOKEN_EXPIRED");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("INVALID_TOKEN");
            return;
        }

        // Continuar si no está autenticado aún
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Extraer la company desde el JWT
            String company = jwtUtil.extractCompany(jwt);

            System.out.println("JWT username: " + username);
            System.out.println("JWT company: " + company);

            // VERIFICACIÓN IMPORTANTE: Asegurarnos de que el username ya no contenga la compañía
            String finalUsername;
            if (username.contains("|")) {
                // Si el username ya tiene formato company|username, usarlo directamente
                finalUsername = username;
                System.out.println("Username already contains company: " + finalUsername);
            } else {
                // Si no, construir el formato compuesto
                finalUsername = company + "|" + username;
                System.out.println("Constructed composite: " + finalUsername);
            }

            CustomUserDetails customUserDetails = (CustomUserDetails) this.userDetailsService.loadUserByUsername(finalUsername);

            if (jwtUtil.isTokenValid(jwt, customUserDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                customUserDetails,
                                null,
                                customUserDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}