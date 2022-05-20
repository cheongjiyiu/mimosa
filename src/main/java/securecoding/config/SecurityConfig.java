package securecoding.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import securecoding.config.services.PostAuthenticationHandler;
import securecoding.model.property.Roles;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public DataSource dataSource;
	@Autowired
	private PostAuthenticationHandler postAuthenticationHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				// Login by user table query
				.usersByUsernameQuery("select username, password, enabled from users where username=? and verified=true")
				.authoritiesByUsernameQuery("select username, role from users where username=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// Allow only admins
				.antMatchers("/api/**", "/h2-console/**", "/management/batches/**").hasAnyAuthority(Roles.ADMIN)
				// Allow privileged
				.antMatchers("/management/**", "/batches/**").hasAnyAuthority(Roles.LECTURER, Roles.ADMIN)
				// Allow authenticated
				.antMatchers("/", "/dashboard", "/challenges/**").authenticated()
				// Allow any other content
				.anyRequest().permitAll()
				// Login configuration
				.and().formLogin().loginPage("/login").successHandler(postAuthenticationHandler)
				// Logout configuration
				.and().logout().logoutSuccessUrl("/login")
				// Allow single instance login
				.and().sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry())
				// Allow h2-console
				.and().and().csrf().ignoringAntMatchers("/h2-console/**")
				.and().headers().frameOptions().sameOrigin();
				// Challenge csrf removal
				//.and().and().csrf().ignoringAntMatchers("/challenges/**/cls/**");
	}

}
