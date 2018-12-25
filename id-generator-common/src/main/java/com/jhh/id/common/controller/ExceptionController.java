package com.jhh.id.common.controller;

import static com.jhh.id.common.exception.handler.MvcExceptionHandler.KEY_SMS_ERROR_CODE;
import static com.jhh.id.common.exception.handler.MvcExceptionHandler.KEY_SMS_ERROR_MESSAGE;

import com.jhh.id.common.enums.ErrorEnum.Global;
import com.jhh.id.common.response.Result;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author renbangjie
 */
@Controller
@RequestMapping("exception")
public class ExceptionController {

  /**
   * 处理Content-Type为application/json类型的异常
   *
   * @return 返回json格式错误信息
   */
  @ResponseBody
  @RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Result<String> jsonView(HttpServletRequest request) {
    return Result.build(Global.ERROR_500,
        String.valueOf(request.getAttribute(KEY_SMS_ERROR_MESSAGE)));
  }

  /**
   * 处理Content-Type为text/html类型的异常,跳转到自定义的错误页面
   *
   * @return 错误页面路径
   */
  @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
  public String htmlView(HttpServletRequest request) {
    return String.valueOf(request.getAttribute(KEY_SMS_ERROR_CODE));
  }

}
