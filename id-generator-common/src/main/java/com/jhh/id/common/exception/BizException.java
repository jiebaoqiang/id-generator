package com.jhh.id.common.exception;

import lombok.Getter;

/**
 * @author renbangjie
 */
@Getter
public class BizException extends RuntimeException {

  private int code;

  public BizException(String message) {
    super(message);
  }

  public BizException(int code, String message) {
    super(message);
    this.code = code;
  }

  public BizException(int code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public BizException(int code, Throwable cause) {
    super(cause);
    this.code = code;
  }

}
