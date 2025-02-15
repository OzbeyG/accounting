package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//
//        List<UserDetails> userList = new ArrayList<>();
//
//        userList.add(
//                 new User("admin", encoder.encode("password1"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//        userList.add(
//                 new User("manager", encoder.encode("password2"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//        return new InMemoryUserDetailsManager(userList);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     return http
             .authorizeRequests()
             //.antMatchers("/user/**").hasRole("ADMIN")
             .antMatchers(
                  "/",
                         "/login",
                         "/fragments/**",
                          "/assets/**",
                           "/images/**"
             ).permitAll()
             .anyRequest().authenticated()
             .and()
             //.httpBasic() //use spring default login pop-up
             .formLogin() //to use app's login page
                  .loginPage("/login")
                  //.defaultSuccessUrl("/dashboard")
                  .successHandler(authSuccessHandler)
                  .failureUrl("/login?error=true")
                  .permitAll()
             .and()
             .logout()
             .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
             .logoutSuccessUrl("/login")
             .and()
             .rememberMe()
                     .tokenValiditySeconds(90)
                     .key("cydeo")
                     .userDetailsService(securityService)
             .and().build();
    }
}
