package gob.regionancash.atencionciudadano.config;

import gob.regionancash.atencionciudadano.model.Tipouser;
import gob.regionancash.atencionciudadano.model.User;
import gob.regionancash.atencionciudadano.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        Claims claims = jwtService.extractAllClaims(jwt);
        userEmail = jwtService.extractUsername(jwt);
        System.out.println(claims);
        //if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        User userDetails = (User) this.userDetailsService.loadUserByUsername(userEmail);
        if(claims.containsKey("directory"))userDetails.setDirectory(Integer.parseInt(claims.get("directory").toString()));
        userDetails.setId(Integer.parseInt(claims.get("uid").toString()));
        List<GrantedAuthority> authorities = new ArrayList<>();
                  /*for (Role role: roles) {
                      authorities.add(new SimpleGrantedAuthority(role.getName()));
                      role.getPrivileges().stream()
                      .map(p -> new SimpleGrantedAuthority(p.getName()))
                      .forEach(authorities::add);
                  }*/
        String[] perms = new String[]{"ADMIN", "USUARIO"};

        for (String perm : perms)
            authorities.add(new SimpleGrantedAuthority(perm));

        userDetails.setAuthorities(authorities);
        if (jwtService.isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        // }
        filterChain.doFilter(request, response);
        //}
    }
}
