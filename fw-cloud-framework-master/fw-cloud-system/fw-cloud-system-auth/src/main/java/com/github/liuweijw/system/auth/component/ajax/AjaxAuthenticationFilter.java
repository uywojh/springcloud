package com.github.liuweijw.system.auth.component.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.github.liuweijw.commons.utils.StringHelper;
import com.github.liuweijw.core.commons.constants.CommonConstant;
import com.github.liuweijw.core.commons.constants.SecurityConstant;

/**
 * Ajax 登录处理过滤器
 * 自定义过滤器
 * 
 * @author wuwei
 */
public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private boolean postOnly = true;

	public AjaxAuthenticationFilter() {
		super(new AntPathRequestMatcher(SecurityConstant.MOBILE_TOKEN_URL, "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals(HttpMethod.POST.name()))
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		String mobile = obtainMobile(request);
		if (StringHelper.isBlank(mobile))
			mobile = "";
		AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(mobile.trim());
		setDetails(request, ajaxAuthenticationToken);
		return this.getAuthenticationManager().authenticate(ajaxAuthenticationToken);
	}

	private String obtainMobile(HttpServletRequest request) {
		return request.getParameter(CommonConstant.SPRING_SECURITY_FORM_MOBILE_KEY);
	}

	private void setDetails(HttpServletRequest request, AjaxAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public boolean isPostOnly() {
		return postOnly;
	}
}


//@Override
//public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//    if (postOnly && !request.getMethod().equals("POST")) {
//        throw new AuthenticationServiceException(
//                "Authentication method not supported: " + request.getMethod());
//    }
//
//    // 登陆类型：user:用户密码登陆；phone:手机验证码登陆；qr:二维码扫码登陆
//    String type = obtainParameter(request, "type");
//    String mobile = obtainParameter(request, "mobile");
//    MyAuthenticationToken authRequest;
//    String principal;
//    String credentials;
//
//    // 手机验证码登陆
//    if("phone".equals(type)){
//        principal = obtainParameter(request, "phone");
//        credentials = obtainParameter(request, "verifyCode");
//    }
//    // 二维码扫码登陆
//    else if("qr".equals(type)){
//        principal = obtainParameter(request, "qrCode");
//        credentials = null;
//    }
//    // 账号密码登陆
//    else {
//        principal = obtainParameter(request, "username");
//        credentials = obtainParameter(request, "password");
//        if(type == null)
//            type = "user";
//    }
//    if (principal == null) {
//        principal = "";
//    }
//    if (credentials == null) {
//        credentials = "";
//    }
//    principal = principal.trim();
//    authRequest = new MyAuthenticationToken(
//            principal, credentials, type, mobile);
//    // Allow subclasses to set the "details" property
//    setDetails(request, authRequest);
//    return this.getAuthenticationManager().authenticate(authRequest);
//}
//
//private void setDetails(HttpServletRequest request,
//                        AbstractAuthenticationToken authRequest) {
//    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
//}
//
//private String obtainParameter(HttpServletRequest request, String parameter) {
//    return request.getParameter(parameter);
//}

