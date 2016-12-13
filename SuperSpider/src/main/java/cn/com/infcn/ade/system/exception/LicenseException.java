/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月22日
 */
package cn.com.infcn.ade.system.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * License异常类
 * @author lihf
 * @date 2016年4月22日
 */
public class LicenseException extends AuthenticationException
{
    private static final long serialVersionUID = 7490395470474829746L;

	public LicenseException() {
		super();
	}

	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseException(String message) {
		super(message);
	}

	public LicenseException(Throwable cause) {
		super(cause);
	}
}
