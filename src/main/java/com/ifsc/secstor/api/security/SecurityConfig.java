package com.ifsc.secstor.api.security;

import com.ifsc.secstor.api.model.Role;
import com.ifsc.secstor.api.security.entrypoint.CustomAuthenticationEntryPoint;
import com.ifsc.secstor.api.security.handler.AuthorizationHandler;
import com.ifsc.secstor.api.security.filter.AuthenticationFilter;
import com.ifsc.secstor.api.security.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.ifsc.secstor.api.advice.paths.Paths.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl(LOGIN_ROUTE);

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(LOGIN_ROUTE + "/**",
                        REFRESH_TOKEN_ROUTE,
                        SAVE_USER_ROUTE,
                        SPLIT_ROUTE,
                        RECONSTRUCT_ROUTE,
                        ANONYMIZATION_ROUTE,
                        GET_USERS_ROUTE,
                        USER_ROUTE);

        http.sessionManagement().sessionCreationPolicy(ALWAYS);

        http.authorizeRequests()
                .antMatchers(REGISTER_ROUTE, LOGIN_ROUTE + "/**", REFRESH_TOKEN_ROUTE, SAVE_USER_ROUTE)
                .permitAll();

        http.authorizeRequests().antMatchers(POST, SPLIT_ROUTE, RECONSTRUCT_ROUTE, ANONYMIZATION_ROUTE)
                .hasAnyAuthority(Role.ADMINISTRATOR.name(), Role.CLIENT.name())
                .and().exceptionHandling().accessDeniedHandler(new AuthorizationHandler())
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.authorizeRequests().antMatchers(GET, GET_USERS_ROUTE).hasAnyAuthority(Role.ADMINISTRATOR.name())
                .and().exceptionHandling().accessDeniedHandler(new AuthorizationHandler())
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.authorizeRequests().antMatchers(GET, USER_ROUTE).hasAnyAuthority(Role.ADMINISTRATOR.name())
                .and().exceptionHandling().accessDeniedHandler(new AuthorizationHandler())
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.authorizeRequests().antMatchers(PUT, USER_ROUTE).hasAnyAuthority(Role.ADMINISTRATOR.name())
                .and().exceptionHandling().accessDeniedHandler(new AuthorizationHandler())
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.authorizeRequests().antMatchers(DELETE, USER_ROUTE).hasAnyAuthority(Role.ADMINISTRATOR.name())
                .and().exceptionHandling().accessDeniedHandler(new AuthorizationHandler())
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(authenticationFilter);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(STATIC_ROUTE)
                .antMatchers(CSS_ROUTE)
                .antMatchers(JS_ROUTE)
                .antMatchers(IMG_ROUTE);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }
}
