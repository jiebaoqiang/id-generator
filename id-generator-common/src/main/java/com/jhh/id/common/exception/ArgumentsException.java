package com.jhh.id.common.exception;

import lombok.Getter;

/**
 * @author renbangjie
 */
@Getter
public class ArgumentsException extends BizException {

  public ArgumentsException(String message) {
    super(message);
  }

  public ArgumentsException(int code, String message) {
    super(code, message);
  }

  public ArgumentsException(int code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public ArgumentsException(int code, Throwable cause) {
    super(code, cause);
  }

}
