package com.github.liuweijw.system.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import com.github.liuweijw.core.configuration.FwUrlsConfiguration;
import com.github.liuweijw.system.auth.component.ajax.AjaxSecurityConfigurer;

/**
 * @author liuweijw 认证服务器开放接口配置
 * 2.3安全配置
 */
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER - 2)
@Configuration
// @EnableResourceServer
@EnableWebSecurity
public class FwResourceServerConfiguration extends WebSecurityConfigurerAdapter {// ResourceServerConfigurerAdapter {

	@Autowired
	private FwUrlsConfiguration		fwUrlsConfiguration;

	@Autowired
	private AjaxSecurityConfigurer	ajaxSecurityConfigurer;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.formLogin()
				// 可以通过授权登录进行访问
				.loginPage("/auth/login")
				.loginProcessingUrl("/auth/signin")//仅仅处理HTTP POST
				.and()
				.authorizeRequests();

//		定义不需要认证就可以访问
		for (String url : fwUrlsConfiguration.getCollects()) {
			registry.antMatchers(url)
					.permitAll();
		}

		registry.anyRequest()
				.authenticated()
				.and()
				.csrf()
				.disable();// 关闭csrf保护功能（跨域访问）
		http.apply(ajaxSecurityConfigurer);
	}

}
