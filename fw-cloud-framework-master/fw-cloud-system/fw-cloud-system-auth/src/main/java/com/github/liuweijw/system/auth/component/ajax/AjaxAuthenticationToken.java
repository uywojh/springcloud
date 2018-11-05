package com.github.liuweijw.system.auth.component.ajax;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * @author liuweijw
 */
public class AjaxAuthenticationToken extends AbstractAuthenticationToken {

	private static final long	serialVersionUID	= SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Object		principal;
	
	public AjaxAuthenticationToken(String mobile) {
		super(null);
		this.principal = mobile;
		setAuthenticated(false);
	}

	public AjaxAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		super.setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated)
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
	}
}


//public class MyAuthenticationToken extends AbstractAuthenticationToken {
//	 
//    private static final long serialVersionUID = 110L;
//    private final Object principal;
//    private Object credentials;
//    private String type;
//    private String mobile;
// 
//    /**
//     * This constructor can be safely used by any code that wishes to create a
//     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link
//     * #isAuthenticated()} will return <code>false</code>.
//     *
//     */
//    public MyAuthenticationToken(Object principal, Object credentials,String type, String mobile) {
//        super(null);
//        this.principal = principal;
//        this.credentials = credentials;
//        this.type = type;
//        this.mobile = mobile;
//        this.setAuthenticated(false);
//    }
// 
//    /**
//     * This constructor should only be used by <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
//     * implementations that are satisfied with producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
//     * token token.
//     *
//     * @param principal
//     * @param credentials
//     * @param authorities
//     */
//    public MyAuthenticationToken(Object principal, Object credentials,String type, String mobile, Collection<? extends GrantedAuthority> authorities) {
//        super(authorities);
//        this.principal = principal;
//        this.credentials = credentials;
//        this.type = type;
//        this.mobile = mobile;
//        super.setAuthenticated(true);
//    }
// 
// 
//    @Override
//    public Object getCredentials() {
//        return this.credentials;
//    }
// 
//    @Override
//    public Object getPrincipal() {
//        return this.principal;
//    }
// 
//    public String getType() {
//        return this.type;
//    }
// 
//    public String getMobile() {
//        return this.mobile;
//    }
// 
//    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//        if(isAuthenticated) {
//            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
//        } else {
//            super.setAuthenticated(false);
//        }
//    }
// 
//    public void eraseCredentials() {
//        super.eraseCredentials();
//        this.credentials = null;
//    }
//}


