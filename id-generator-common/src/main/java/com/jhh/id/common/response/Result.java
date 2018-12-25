package com.jhh.id.common.response;


import com.jhh.id.common.enums.ErrorEnum.ErrorCode;
import com.jhh.id.common.enums.ErrorEnum.Global;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author renbangjie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = 869844584448910764L;

  private int code;

  private String msg;

  private T data;

  private long time;

  public static <T extends Serializable> Result<T> success() {
    return success(null);
  }

  public static <T extends Serializable> Result<T> success(T data) {
    return build(Global.SUCCESS, "OK", data);
  }

  public static <T extends Serializable> Result<T> build(ErrorCode errorCode,
      String msg) {
    return build(errorCode, msg, null);
  }

  public static <T extends Serializable> Result<T> build(ErrorCode errorCode,
      String msg, T data) {
    return new Result<>(errorCode.getCode(), msg, data,
        System.currentTimeMillis());
  }

}
