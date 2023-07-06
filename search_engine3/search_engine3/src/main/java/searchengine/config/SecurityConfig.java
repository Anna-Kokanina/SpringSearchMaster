package searchengine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// This class is for configuring security settings
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${app.credentials.user.name}")
    private String username;

    @Value("${app.credentials.user.password}")
    private String userPassword;

    @Value("${app.credentials.admin.name}")
    private String adminName;

    @Value("${app.credentials.admin.password}")
    private String adminPassword;

    // Bean for creating and storing user details in memory
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
        // Constructing a standard user with role "USER"
        UserDetails standardUser = createUser(username, userPassword, passwordEncoder, "USER");

        // Constructing an admin user with role "ADMIN"
        UserDetails adminUser = createUser(adminName, adminPassword, passwordEncoder, "ADMIN");

        // Returning a manager containing the standard and admin users
        return new InMemoryUserDetailsManager(standardUser, adminUser);
    }

    // Method to create a user with given username, password, role and password encoder
    private UserDetails createUser(String username, String password, PasswordEncoder passwordEncoder, String role) {
        return User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles(role)
                .build();
    }

    // Bean for configuring and building a SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity httpSecurity) throws Exception {
        configureHttpSecurity(httpSecurity);
        // Building and returning the SecurityFilterChain
        return httpSecurity.build();
    }

    // Method to configure HttpSecurity object
    private void configureHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .csrf()
                .disable();
    }

    // Bean for creating a PasswordEncoder
    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
