package com.gscdz.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.gscdz.handler.CustomAuthenticationSuccessHandler;
import com.gscdz.handler.MyAcessDeniedHandler;
import com.gscdz.service.UserDetailService;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	@Autowired
	private UserDetailService userDetailsService;
	@Autowired
    private DataSource dataSource;
	@Autowired
	private MyAcessDeniedHandler accessDeniedHandler;
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//登录登出路径
		http
			.formLogin()
			.loginPage("/login").loginProcessingUrl("/login").successHandler(customAuthenticationSuccessHandler)
			.failureUrl("/login?error=true")//验证失败重定向
		.and()
			.logout().permitAll()
		.and()
			.headers().frameOptions().sameOrigin();
		;
		//业务路径
		http
			.authorizeRequests()
			.antMatchers("/login","/registerPage","/user/checkUsername","/user/register","/getAdminHome").permitAll()
			.antMatchers("/user/getAvatar","/user/getSelfInformation","/quiz/**","/solvedProblem/**","/chapter/**").hasAnyRole("USER","ADMIN")
			.antMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated();
		//关闭csrf防护
		http
			.csrf().disable().httpBasic();
		//rememberMe
		http
			.rememberMe()
			.rememberMeParameter("isRememberMe")//前端页面对应的checkbox的name
			.tokenRepository(getPersistentTokenRepository())
			.tokenValiditySeconds(60*60*24*7)//Token过期时间为一周
			.userDetailsService(userDetailsService);
		//重写forbidden页面
		http
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler);
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/asset/**");//静态资源
	}
	@Bean
	public PasswordEncoder getpswEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	@Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        //jdbcTokenRepositoryImpl.setCreateTableOnStartup(true);
        return jdbcTokenRepositoryImpl;
    } 

}
