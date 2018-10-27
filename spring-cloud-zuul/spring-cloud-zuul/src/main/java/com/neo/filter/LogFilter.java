package com.neo.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 打印请求日志
 * 记录请求日志的过滤器
 * @description 记录请求日志的过滤器
 * @author wuwei
 * @version 1.0
 * @date:2018年8月23日下午3:58:55
 */
@Component
public class LogFilter extends ZuulFilter {
	private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		LogFilter.LOG.info(String.format("打印日志 ： send %s request to %s", request.getMethod(), request.getRequestURL().toString()));
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 2;
	}

	@Override
	public String filterType() {
		return "pre";
	}
}
