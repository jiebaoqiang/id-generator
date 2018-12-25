package com.jhh.id.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements ApplicationContextAware {

  private static ApplicationContext ctx;

  public static <T> T getBean(Class<T> beanClass) {
    return ctx.getBean(beanClass);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    init(applicationContext);
  }

  private static void init(ApplicationContext applicationContext) {
    ctx = applicationContext;
  }

}
