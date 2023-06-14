package com.zanchenko.alexey.JWT.Authentication.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailService userDetailService;
    @Autowired
    public SecurityConfig(CustomUserDetailService userDetailService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailService = userDetailService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ // we have already tackled our JWT authentication entry point
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() // possible to permit by roles (secure route y roll)
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFIlter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean // userDetailsService
//    public UserDetailsService users(){ // is going to return a user Detail service
//        // and here's where we are going to implement an actual in-memory user
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.builder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager();
//    }

    // we comment it because what's happening is that if
    //that security can fit if the user Detail
    //Service and the security config is still
    //there it overrides our custom user
    //Detail Service and makes it so that we
    //can't log in so go ahead delete that uh
    //the user Detail Service that we coded up
    //in the very beginning just make sure to
    //delete that I'm going to go ahead and
    //run it and prove to everybody that works
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //let's go ahead also and bring
    //in our password encoder
    //so if you don't know what a password
    //encoder is if you store plain text
    //passwords in 2020 that is a very very
    //bad idea because people can break into
    //your database so we have this thing
    //called a password encoder that is going
    //to make it so that whenever you store
    //data or whenever you store passwords in
    //a database
    //it stores them not as the actual
    //password it stores them as stream
    //unintelligible strings so that if
    //anybody were able to ever break into our
    //database what will happen is they would
    //not be able to uh get the passwords that
    //easily they would have to essentially
    //use really sophisticated algorithms to
    //try to decode AK uh hence in code there
    //try to decode these passwords so that
    //you won't be able to actually use them
    //if the database was ever breached and
    //luckily we don't have to worry about a
    //lot of that only thing that we have to
    //do is worry about our password encoder
    //and we're going to be using B Curt
    //password encoder Spring Security takes
    //care of pretty much all of it you don't
    //have to worry about so much and uh it
    //takes it takes a lot of the actual leg
    //work out of uh making sure that the
    //passwords are actually encoded anyways

    @Bean
    public JWTAuthenticationFIlter jwtAuthenticationFIlter(){
        return new JWTAuthenticationFIlter();
    }
}
