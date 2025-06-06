package de.whs.wi.friends_and_places.config;

import de.whs.wi.friends_and_places.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtRequestFilter is a filter that checks for JWT tokens in incoming requests.
 * It extracts the token, validates it, and sets the authentication in the security context.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method is called for every request to check if the JWT token is valid and set the authentication in the security context.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            String email = null;
            String jwtToken = null;

            // Log the request URL and authorization header for debugging
            logger.info("Processing request: {} {}", request.getMethod(), request.getRequestURI());

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                try {
                    email = jwtUtil.extractUsername(jwtToken);
                    logger.info("JWT Token extracted email: {}", email);
                } catch (Exception e) {
                    logger.error("Invalid JWT token: {}", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"Invalid JWT token: " + e.getMessage() + "\"}");
                    response.setContentType("application/json");
                    return;
                }
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    logger.info("Loaded user details for: {}", email);

                    if (jwtUtil.validateToken(jwtToken, userDetails)) {
                        logger.info("JWT Token validated successfully for user: {}", email);

                        // Create an authentication token with proper details
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("Set authentication in SecurityContext");
                    } else {
                        logger.warn("JWT Token validation failed for user: {}", email);
                    }
                } catch (Exception e) {
                    logger.error("Error during authentication: {}", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"Authentication error: " + e.getMessage() + "\"}");
                    response.setContentType("application/json");
                    return;
                }
            }

            // Continue the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error in JWT filter: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Internal server error in authentication filter\"}");
            response.setContentType("application/json");
        }
    }
}
