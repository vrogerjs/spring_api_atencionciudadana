package gob.regionancash.atencionciudadano.config;

//import gob.regionancash.atencionciudadano.user.UserRepository;
import gob.regionancash.atencionciudadano.model.User;
import lombok.RequiredArgsConstructor;

import java.util.*;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {



    @Bean
    public UserDetailsService userDetailsService() {

        return new UserDetailsService(){

          @Override
          public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
              System.out.println("loadUserByUsername "+username);
          //username -> {
        
          //read from db info from user and permision
                /*repository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));*/
            User u = new User();
            u.setName(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            String[] perms = new String[]{};
            for (String perm : perms)
                authorities.add(new SimpleGrantedAuthority(perm));
            u.setAuthorities(authorities);
            return u;
        }
      };
    };
    
  @Bean
    public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
