package sg.edu.ntu.nutrimate.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private WebApplicationContext applicationContext;

    // @Autowired
    // private AuthenticationSuccessHandlerImpl successHandler;

    // @Autowired
    // private DataSource dataSource;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    // @Autowired
    // private AppBasicAuthenticationEntryPoint authenticationEntryPoint;

    // @Bean
    // public UserDetailsManager users(HttpSecurity http) throws Exception {
    // AuthenticationManager authenticationManager =
    // http.getSharedObject(AuthenticationManagerBuilder.class)
    // .userDetailsService(userDetailsService)
    // .passwordEncoder(passwordEncoder())
    // .and()
    // .authenticationProvider(authenticationProvider())
    // .build();
    // }

    @PostConstruct
    public void completeSetup() {
        userDetailsService = applicationContext.getBean(CustomUserDetailsService.class);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .and()
                .headers()
                .frameOptions().disable()                
                .and()
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/static/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/h2/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/nutrimate/public/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/nutrimate/customers/**")
                .hasRole("user")
                .and()
                .authorizeRequests()
                .antMatchers("/nutrimate/admin/**")
                .hasRole("admin")
                .and()
                .authorizeRequests()
                .antMatchers("/nutrimate/basicauth")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/signin*")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/index")
                .hasAnyRole("user", "admin")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(new AuthenticationEntryPoint() { // << implementing this interface
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response,
                            AuthenticationException authException) throws IOException, ServletException {
                        // >>> response.addHeader("WWW-Authenticate", "Basic realm=\"" + realmName +
                        // "\""); <<< (((REMOVED)))
                             
                        // Using DaoAuthenticationProvider, the authentication message is thrown by method additionalAuthenticationChecks()                 
                        String message = authException.getMessage();
                        if (loginAttemptService.isBlocked()) {
                            message = "Your account is blocked as you have exceeded the number of tries.";
                        }
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        PrintWriter writer = response.getWriter();
                        writer.println(message);
                        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        
                        // response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    }
                })
                .and()
                
                // For ReactUI************************************
                .logout()
                // .logoutSuccessUrl("/")
                .logoutUrl("/nutrimate/logout")
                .invalidateHttpSession(true)                
                .deleteCookies("JSESSIONID")
                // .logoutSuccessUrl("/nutrimate/success_logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
                // For ReactUI************************************

                // For ThymeLeaf UI ************************************
                // .formLogin()
                // .loginPage("/signin")
                // .loginProcessingUrl("/perform_login")
                // .defaultSuccessUrl("/index", true)
                // // .failureUrl("/auth-error?error=true")
                // .failureUrl("/signin-error.html")
                // .failureHandler(authenticationFailureHandler())
                // .and()
                // .rememberMe()
                // .userDetailsService(userDetailsService)
                // .rememberMeParameter("remember-me")
                // .key("uniqueAndSecret")
                // .tokenValiditySeconds(86400)
                // .and()
                // .logout()
                // // .logoutUrl("/perform_logout")
                // .deleteCookies("JSESSIONID", "remember-me")
                // .logoutSuccessUrl("/signin?logout");
                // // .logoutSuccessHandler(logoutSuccessHandler());
                // For ThymeLeaf UI ************************************

        // ******************** Required for servlets-based web login
        // *************************/
        // .formLogin()
        // .loginPage("/signin")
        // .loginProcessingUrl("/perform_login")
        // .defaultSuccessUrl("/index", true)
        // // .failureUrl("/auth-error?error=true")
        // .failureUrl("/signin-error.html")
        // .failureHandler(authenticationFailureHandler())
        // .and()
        // .rememberMe()
        // .userDetailsService(userDetailsService)
        // .rememberMeParameter("remember-me")
        // .key("uniqueAndSecret")
        // .tokenValiditySeconds(86400)
        // .and()
        // .logout()
        // // .logoutUrl("/perform_logout")
        // .deleteCookies("JSESSIONID", "remember-me")
        // .logoutSuccessUrl("/signin?logout");
        // // .logoutSuccessHandler(logoutSuccessHandler());
        // ******************** Required for servlets-based web login
        // *************************/

        /************ Not reuqired ***************/
        // .authenticationEntryPoint(authenticationEntryPoint);
        // http.logout((logout) -> logout.logoutUrl("/nutrimate/customers/logout"));
        /************ Not reuqired ***************/

        return http.build();
    }

    // @Bean
    // public InMemoryUserDetailsManager userDetailsService() {
    // UserDetails user = User
    // .withUsername("user")
    // .password(passwordEncoder().encode("password"))
    // .roles("user")
    // .build();

    // UserDetails admin = User
    // .withUsername("admin")
    // .password(passwordEncoder().encode("administrator"))
    // .roles("admin")
    // .build();
    // return new InMemoryUserDetailsManager(user, admin);
    // }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}