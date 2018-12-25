package com.jhh.id.conf;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jhh.id.service.IdService;
import com.jhh.id.service.impl.IdServiceImpl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:generator.properties")
@ConfigurationProperties
public class GeneratorConf {


  @Bean
  IdService idService(ExecutorService executorService,
      GeneratorConf generatorConf) {
    return new IdServiceImpl(generatorConf.getThreshold(), executorService);
  }


  @Bean(destroyMethod = "shutdown")
  ExecutorService executorService(GeneratorConf generatorConf) {
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("idGenerator-pool-%d")
        .build();
    return new ThreadPoolExecutor(generatorConf.getCoreSize(),
        generatorConf.getMaxSize(),
        generatorConf.getKeepAliveTime(), TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(generatorConf.getCapacity()), threadFactory);
  }


  @Value("${threshold:0.5}")
  private Double threshold;

  @Value("${thread-pool.queue.capacity:100}")
  private Integer capacity;

  @Value("${thread-pool.core-size:3}")
  private Integer coreSize;

  @Value("${thread-pool.max-size:10}")
  private Integer maxSize;

  @Value("${thread-pool.keep-alive-time:30000}")
  private Long keepAliveTime;


}
