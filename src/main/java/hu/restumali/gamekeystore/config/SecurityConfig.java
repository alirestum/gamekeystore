package hu.restumali.gamekeystore.config;

import hu.restumali.gamekeystore.model.UserRoleType;
import hu.restumali.gamekeystore.security.GKSAuthenticationSuccessHandler;
import hu.restumali.gamekeystore.security.GKSLogoutSuccessHandler;
import hu.restumali.gamekeystore.service.GKSUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    GKSUserDetailsService userDetailsService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole(UserRoleType.WebshopAdmin.toString())
                .antMatchers("/css/**", "/").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/user/**").hasRole(UserRoleType.Customer.toString())

                    .and()
                .formLogin().loginPage("/user/login").loginProcessingUrl("/user/login").successHandler(getAuthSuccessHandler())
                    .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")).logoutSuccessHandler(getLogoutSuccessHandler())
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationSuccessHandler getAuthSuccessHandler() { return new GKSAuthenticationSuccessHandler(); }

    @Bean
    public LogoutSuccessHandler getLogoutSuccessHandler() { return new GKSLogoutSuccessHandler(); }

}
