package gob.regionancash.atencionciudadano.config;

import gob.regionancash.atencionciudadano.model.TipoUser;
import gob.regionancash.atencionciudadano.model.User;
import gob.regionancash.atencionciudadano.repository.UserRepository;
import gob.regionancash.atencionciudadano.service.AccessService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final AccessService accessService;
    
    private final UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        username = jwtService.getUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            User userDetails = (User) this.userDetailsService.loadUserByUsername(username);
            /*
             Save user info from token
             */
            Map claims = jwtService.getClaims(jwt);

            userDetails.setId(Integer.parseInt(claims.get("uid").toString()));

            if(claims.containsKey("directory"))
                userDetails.setDirectory(Integer.parseInt(claims.get("directory").toString()));

            if (jwtService.isTokenValid(jwt, userDetails)) {

                List<GrantedAuthority> authorities = new ArrayList<>();
                for (String perm : accessService.perms(jwt))
                    if(perm.length()>0)
                    authorities.add(new SimpleGrantedAuthority(perm));

                //remove when production
                authorities.add(new SimpleGrantedAuthority("REGISTER_ATENCION_CIUDADANA"));

                userDetails.setAuthorities(authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else{
                throw new RuntimeException("invalid token!");
            }
            
        }
        filterChain.doFilter(request, response);
        //}
    }
}
