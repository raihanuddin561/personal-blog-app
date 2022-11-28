package com.mobileappws.developerBlog.security;
import com.mobileappws.developerBlog.constants.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity  {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
       http
                .csrf()
                .disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .authorizeHttpRequests((auth)->
                       auth
                               .antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL).permitAll()
                               .antMatchers(HttpMethod.GET,SecurityConstants.VERIFY_EMAIL).permitAll()
                               .anyRequest().authenticated())
               .addFilter(new CustomAuthenticationFilter(authenticationManager))
               .addFilterBefore(new CustomAuthorizationFilter(),UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }




}
