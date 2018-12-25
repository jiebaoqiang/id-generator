package com.jhh.id.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理xss攻击
 */
@Slf4j
public class AccessLogFilter implements Filter {


  @Override
  public void init(FilterConfig filterConfig) {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    long start = System.currentTimeMillis();
    chain.doFilter(request, response);
    if (log.isDebugEnabled()) {
      log.info("请求:{},耗时:{}ms", req.getRequestURI(),
          System.currentTimeMillis() - start);
    }
  }

  @Override
  public void destroy() {

  }
}
