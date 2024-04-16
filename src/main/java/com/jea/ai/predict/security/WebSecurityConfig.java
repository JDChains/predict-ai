package com.jea.ai.predict.security;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   // @Override
    protected void configures(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .httpBasic();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/**").permitAll()  // Allow all requests by matching all paths
                .anyRequest().authenticated()
            .and()
            .csrf().disable();  // Disable CSRF protection for development ease

        // Optionally, you can also disable session creation on Spring Security
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add additional configuration to disable security features as needed
        http.headers().frameOptions().disable();  // For H2 console access if in use
    }
    
}
