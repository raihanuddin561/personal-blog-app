package com.appsdeveloperblog.app.ws.security;
import com.appsdeveloperblog.app.ws.constants.SecurityConstants;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity  {

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserServiceImpl();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,SecurityConstants.LOGIN)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authentication -> authentication));
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(SecurityConstants.SIGN_UP_URL)
                .antMatchers(SecurityConstants.LOGIN);
    }



}
