package be.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        String username = jwtTokenUtil.extractUsername(jwt);

        logger.info("JWT Authentication Filter: Extracted username from token: {}", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info("JWT Authentication Filter: User details retrieved for: {}", username);

            if (jwtTokenUtil.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, jwt, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("JWT Authentication Filter: Authentication set for user: {}", username);

                // Check access for the requested URI
                String requestUri = request.getRequestURI();

                // Allow access to log out endpoint without role checks
                if (requestUri.endsWith("/auth/logout")) {
                    // Permit logout for any authenticated user
                    filterChain.doFilter(request, response);
                    return; // Exit early for logout requests
                }

                // Allow access to messages endpoint without role checks
                if (requestUri.startsWith("/messages")) {
                    filterChain.doFilter(request, response);
                    return; // Exit early for messages requests
                }

                // Check access for other requests
                if (!hasAccess(userDetails, requestUri)) {
                    logger.warn("JWT Authentication Filter: Access denied for user: {} to URI: {}", username, requestUri);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return; // Stop further processing if access is denied
                }

            }
            else {
                logger.warn("JWT Authentication Filter: Token invalid for user: {}", username);
            }
        }
        else {
            logger.warn("JWT Authentication Filter: Username null or already authenticated.");
        }

        filterChain.doFilter(request, response);
    }

    private boolean hasAccess(UserDetails userDetails, String requestUri) {
        // Log user roles
        logger.info("Checking access for user: {} with roles: {}", userDetails.getUsername(), userDetails.getAuthorities());

        // Check if the user has specific roles
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isCollector = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_COLLECTOR"));
        boolean isResident = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_RESIDENT"));
        boolean isRecyclingDepot = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_RECYCLING_DEPOT"));

        // Log role checks
        logger.info("Roles: Admin: {}, Collector: {}, Resident: {}", isAdmin, isCollector, isResident);


        // Define access logic based on roles and requested URI
        if (isAdmin) {
            return true; // Admin has access to everything
        } else if (isCollector && requestUri.startsWith("/collector")) {
            return true; // Collector has access to collector-related URIs
        } else if (isResident && requestUri.startsWith("/resident")) {
            return true; // Resident has access to resident-related URIs
        } else if (isRecyclingDepot && requestUri.startsWith("/recycling_depot")) {
            return true; // Resident has access to resident-related URIs
        }

        return requestUri.startsWith("/public"); // Public URIs are accessible to everyone
    }
}

