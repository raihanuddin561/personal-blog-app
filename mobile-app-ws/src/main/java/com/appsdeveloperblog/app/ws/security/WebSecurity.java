package com.appsdeveloperblog.app.ws.security;
import com.appsdeveloperblog.app.ws.constants.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class WebSecurity  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable().authorizeHttpRequests((authz) -> {
                            try {
                                authz
                                        .antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL)
                                        .permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilter(getAuthenticationFilter())
                                        .addFilter(new AuthorizationFilter(authentication -> authentication))
                                        .sessionManagement()
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .httpBasic(Customizer.withDefaults())
                .passwordManagement(Customizer.withDefaults())
                ;
        return http.build();
    }
   /* @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL)
              //  .antMatchers(HttpMethod.POST,SecurityConstants.LOGIN);
    }*/

    public AuthenticationFilter getAuthenticationFilter(){
        final AuthenticationFilter filter = new AuthenticationFilter(authentication -> authentication);
        filter.setFilterProcessesUrl(SecurityConstants.LOGIN);
        return filter;
    }
}
