package com.jhh.id.common.exception.handler;

import com.jhh.id.common.enums.ErrorEnum.Global;
import com.jhh.id.common.exception.BizException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Slf4j
public class MvcExceptionHandler extends DefaultHandlerExceptionResolver {

  public static final String KEY_SMS_ERROR_CODE = "sms.error.code";
  public static final String KEY_SMS_ERROR_MESSAGE = "sms.error.message";

  @Override
  protected ModelAndView doResolveException(
      HttpServletRequest request,
      HttpServletResponse response, Object handler, Exception ex) {
    log.error(ex.getMessage(), ex);
    String msg =
        (ex instanceof BizException) ? ex.getMessage() : "系统繁忙,请稍后再试.";
    try {
      request.setAttribute(KEY_SMS_ERROR_CODE, Global.ERROR_500);
      request.setAttribute(KEY_SMS_ERROR_MESSAGE, msg);
      request.getRequestDispatcher("/exception/").forward(request, response);
    } catch (ServletException | IOException e) {
      log.error(e.getMessage(), e);
    }
    return new ModelAndView();
  }
}
