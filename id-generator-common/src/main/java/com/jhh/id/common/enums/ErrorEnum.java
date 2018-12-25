package com.jhh.id.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ErrorEnum {

  /**
   * 全局系统异常,异常码在1000以内
   */
  @Getter
  @AllArgsConstructor
  public enum Global implements ErrorCode {

    SUCCESS(200, "OK"),
    ERROR_500(500, "系统异常");

    private int code;

    private String desc;

  }

  /**
   * 业务异常，错误码请确保大于1000
   */
  @Getter
  @AllArgsConstructor
  public enum Biz implements ErrorCode {

    ERROR_1001(1001, "提交短信数据到第三方接口失败"),
    ERROR_1002(1002, "短信通道不存在或不可用"),
    ERROR_1003(1003, "短信模板不存在或不可用"),
    ERROR_1004(1004, "短信模板格式异常");

    private int code;

    private String desc;

  }

  /**
   * 异常编码规范
   */
  public interface ErrorCode {

    /**
     * 获取错误码
     */
    int getCode();

  }

}
