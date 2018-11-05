package com.github.liuweijw.system.auth.component.ajax;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.liuweijw.system.api.UserFeignApi;
import com.github.liuweijw.system.api.model.AuthUser;
import com.github.liuweijw.system.auth.service.UserDetailsImpl;

/**
 * 
 *AjaxAuthenticationProvider类的责任是: 
 *1. 对用户凭证与 数据库、LDAP或其他系统用户数据，进行验证。 
 *2. 如果用户名和密码不匹配数据库中的记录，身份验证异常将会被抛出。 
 *3. 创建用户上下文，你需要一些你需要的用户数据来填充（例如 用户名 和用户密码） 
 *4. 在成功验证委托创建JWT令牌的是在* AjaxAwareAuthenticationSuccessHandler* 中实现。
 *
 * 自定义参数验证 <br/>
 * 可以在自定义登录界面添加登录时需要的参数，如多个验证码等、可以修改默认登录名称和密码的参数名 整体流程：<br/>
 * 1.用户登录时，先经过自定义的passcard_filter过滤器，该过滤器继承了AbstractAuthenticationProcessingFilter，并且绑定了登录失败和成功时需要的处理器(跳转页面使用)<br/>
 * 2.执行attemptAuthentication方法，可以通过request获取登录页面传递的参数，实现自己的逻辑，并且把对应参数set到AbstractAuthenticationToken的实现类中<br/>
 * 3.验证逻辑走完后，调用 this.getAuthenticationManager().authenticate(token);方法，执行AuthenticationProvider的实现类的supports方法<br/>
 * 4.如果返回true则继续执行authenticate方法<br/>
 * 5.在authenticate方法中，首先可以根据用户名获取到用户信息，再者可以拿自定义参数和用户信息做逻辑验证，如密码的验证<br/>
 * 6.自定义验证通过以后，获取用户权限set到User中，用于springSecurity做权限验证<br/>
 * 7.this.getAuthenticationManager().authenticate(token)方法执行完后，会返回Authentication，如果不为空，则说明验证通过<br/>
 * 8.验证通过后，可实现自定义逻辑操作，如记录cookie信息<br/>
 * 9.attemptAuthentication方法执行完成后，由springSecuriy来进行对应权限验证，成功于否会跳转到相对应处理器设置的界面。<br/>
 * 
 * @author liuweijw
 */
public class AjaxAuthenticationProvider implements AuthenticationProvider {

	private UserFeignApi	userFeignApi;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		AjaxAuthenticationToken ajaxAuthenticationToken = (AjaxAuthenticationToken) authentication;
		AuthUser user = userFeignApi.findUserByMobile((String) ajaxAuthenticationToken.getPrincipal());
		if (null == user)
			throw new UsernameNotFoundException("登录账户[" + ajaxAuthenticationToken.getPrincipal() + "]不存在");
		
		UserDetailsImpl userDetails = buildUserDeatils(user);
		if (null == userDetails)
			throw new InternalAuthenticationServiceException("登录用户[" + ajaxAuthenticationToken.getPrincipal() + "]不存在！");

		AjaxAuthenticationToken authenticationToken = new AjaxAuthenticationToken(userDetails, userDetails.getAuthorities());
		authenticationToken.setDetails(ajaxAuthenticationToken.getDetails());
		return authenticationToken;
	}

	private UserDetailsImpl buildUserDeatils(AuthUser authUser) {
		return new UserDetailsImpl(authUser);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public UserFeignApi getUserFeignApi() {
		return userFeignApi;
	}

	public void setUserFeignApi(UserFeignApi userFeignApi) {
		this.userFeignApi = userFeignApi;
	}

}


//public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    // 此处修改断言自定义的 MyAuthenticationToken
//    Assert.isInstanceOf(MyAuthenticationToken.class, authentication, this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.onlySupports", "Only MyAuthenticationToken is supported"));
//    // ...
//}
//
//protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
//    MyAuthenticationToken result = new MyAuthenticationToken(principal, authentication.getCredentials(),((MyAuthenticationToken) authentication).getType(),((MyAuthenticationToken) authentication).getMobile(), this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
//    result.setDetails(authentication.getDetails());
//    return result;
//}


