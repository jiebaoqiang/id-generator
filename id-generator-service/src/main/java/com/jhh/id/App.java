package com.jhh.id;

import com.jhh.id.common.controller.ExceptionController;
import com.jhh.id.common.exception.handler.MvcExceptionHandler;
import com.jhh.id.common.filter.AccessLogFilter;
import com.jhh.id.conf.GeneratorConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@SpringBootApplication
@Import(GeneratorConf.class)
public class App {

  @Bean
  MvcExceptionHandler mvcExceptionHandler() {
    return new MvcExceptionHandler();
  }

  @Bean
  ExceptionController exceptionController() {
    return new ExceptionController();
  }

  @Bean
  AccessLogFilter accessLogFilter() {
    return new AccessLogFilter();
  }


  @Bean
  public RedisTemplate<Object, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<Object, Object> template = new RedisTemplate<>();
    template
        .setKeySerializer(new StringRedisSerializer());
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }


  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

}
