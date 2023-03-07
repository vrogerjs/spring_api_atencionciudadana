package gob.regionancash.atencionciudadano;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityWebAuxTestConfig {

    @Bean
    public UserDetailsService userDetailsService23() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
          .withUsername("user")
          .password(
            //encoder().encode
            ("userPass")
            )
          .roles("USER").build());
        manager.createUser(User
          .withUsername("admin")
          .password(
            //encoder().encode
            ("adminPass"))
          .roles("ADMIN_").build());
        return manager;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}