package finemytrip.backend.config;

import finemytrip.backend.util.JwtUtil;
import finemytrip.backend.util.TokenBlacklist;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String authHeader = request.getHeader("Authorization");
        
        log.info("=== JWT Filter Debug ===");
        log.info("Request URI: {}", requestURI);
        log.info("Request Method: {}", method);
        log.info("Is public path: {}", isPublicPath(requestURI, method));
        
        if (isPublicPath(requestURI, method)) {
            log.info("Skipping JWT validation for public path: {} {}", method, requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        
        log.info("Authentication required for: {} {}", method, requestURI);
        log.info("Authorization header: {}", authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("JWT Token: {}", token);
            
            if (jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token) && !tokenBlacklist.isBlacklisted(token)) {
                String email = jwtUtil.getEmailFromToken(token);
                log.info("Email from token: {}", email);
                
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    log.info("UserDetails loaded: {}", userDetails.getUsername());
                    
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Authentication set in SecurityContext");
                } catch (Exception e) {
                    log.error("Error loading user details: {}", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\":\"Invalid token.\"}");
                    return;
                }
            } else {
                log.warn("Token validation failed - valid: {}, expired: {}, blacklisted: {}", 
                    jwtUtil.validateToken(token), 
                    jwtUtil.isTokenExpired(token), 
                    tokenBlacklist.isBlacklisted(token));
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"Invalid token.\"}");
                return;
            }
        } else {
            log.warn("No valid Authorization header found for protected endpoint: {} {}", method, requestURI);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Authentication required.\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isPublicPath(String requestURI, String method) {
        log.info("Checking if path is public: {} {}", method, requestURI);
        
        // Perform the upload path check first
        if (requestURI.startsWith("/uploads/") || requestURI.equals("/uploads")) {
            log.info("Matched uploads path: {}", requestURI);
            return true;
        }
        
        if (requestURI.equals("/") && "GET".equals(method)) {
            log.info("Matched root path");
            return true;
        }
        if (requestURI.equals("/api/members/register") && "POST".equals(method)) {
            log.info("Matched register path");
            return true;
        }
        if (requestURI.equals("/api/members/login") && "POST".equals(method)) {
            log.info("Matched login path");
            return true;
        }
        
        if (requestURI.equals("/h2-console") || requestURI.startsWith("/h2-console/")) {
            log.info("Matched h2-console path");
            return true;
        }
        
        if (!"GET".equals(method)) {
            log.info("Not a GET request, authentication required");
            return false;
        }
        
        if (requestURI.equals("/api/members")) {
            log.info("Matched members list path");
            return true;
        }
        
        if (requestURI.matches("/api/members/\\d+")) {
            log.info("Matched member by id path: {}", requestURI);
            return true;
        }
        
        if (requestURI.startsWith("/api/members/email/")) {
            log.info("Matched member by email path: {}", requestURI);
            return true;
        }
        
        if (requestURI.equals("/api/main-slides")) {
            log.info("Matched main slides list path");
            return true;
        }
        if (requestURI.matches("/api/main-slides/\\d+")) {
            log.info("Matched main slide by id path: {}", requestURI);
            return true;
        }
        
        if (requestURI.equals("/api/products")) {
            log.info("Matched products list path");
            return true;
        }
        if (requestURI.matches("/api/products/\\d+")) {
            log.info("Matched product by id path: {}", requestURI);
            return true;
        }
        
        log.info("Path is NOT public: {} {}", method, requestURI);
        return false;
    }
} 