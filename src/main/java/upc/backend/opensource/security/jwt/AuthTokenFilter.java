package upc.backend.opensource.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import upc.backend.opensource.security.services.UserDetailsServiceImpl;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Log para ver si el filtro se activa
        System.out.println("====== AuthTokenFilter: INICIANDO FILTRO PARA URL: " + request.getRequestURI() + " ======");

        try {
            String jwt = parseJwt(request);

            // Verificamos si el token existe y es válido
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                System.out.println("====== AuthTokenFilter: Token JWT es válido. ======");

                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                System.out.println("====== AuthTokenFilter: Username extraído: " + username + " ======");

                // Intentamos cargar los detalles del usuario desde la BD
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("====== AuthTokenFilter: UserDetails cargado para " + username + " ======");
                logger.info("Usuario autenticado: {}", username);
                logger.info("Roles del usuario: {}", userDetails.getAuthorities());
                // Creamos la autenticación
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Seteamos la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("====== AuthTokenFilter: Usuario autenticado y seteado en SecurityContext. ======");

            } else {
                if (jwt == null) {
                    System.out.println("====== AuthTokenFilter: No se encontró JWT en el encabezado. ======");
                } else {
                    System.out.println("====== AuthTokenFilter: La validación del token falló. ======");
                }
            }
        } catch (Exception e) {
            // Si algo falla, imprimimos la excepción
            System.err.println("====== AuthTokenFilter: EXCEPCIÓN INESPERADA: " + e.getMessage() + " ======");
            logger.error("No se puede setear la autenticación del usuario: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
