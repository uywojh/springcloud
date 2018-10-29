package com.neo.filter;
 
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.servlet.http.HttpServletRequest;
 
/**
 * 第二个前置过滤器
 * @author wuwei
 *
 */
public class SecondFilter extends ZuulFilter {
 
    private static Logger log = LoggerFactory.getLogger(SecondFilter.class);
 
    @Override
    public String filterType() {
        //前置过滤器
        return "pre";
    }
 
    @Override
    public int filterOrder() {
        //优先级，数字越大，优先级越低
        return 1;
    }
 
    @Override
    public boolean shouldFilter() {
        //是否执行该过滤器，true代表需要过滤
        return true;
    }
 
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("second过滤器");
        String psd = request.getParameter("psd");// 获取请求的参数
        if (StringUtils.isNotBlank(psd)) {
            ctx.setSendZuulResponse(true); //对请求进行路由
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);
            return null;
        } else {
            ctx.setSendZuulResponse(false); //不对其进行路由
            ctx.setResponseStatusCode(400);
            ctx.setResponseBody("token is empty");
            ctx.set("isSuccess", false);
            return null;
        }
 
    }
 
}

