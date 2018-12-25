package com.jhh.id.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class IdEnums {

  @Getter
  @AllArgsConstructor
  public enum Type {

    DB(0, ""),
    REDIS(1, "redis自增");

    private int code;
    private String desc;

  }

}