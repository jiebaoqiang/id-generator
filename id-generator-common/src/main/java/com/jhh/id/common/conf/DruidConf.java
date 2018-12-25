package com.jhh.id.common.conf;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import java.util.Arrays;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidConf {

  @ConfigurationProperties(prefix = "spring.druid")
  @Bean(initMethod = "init", destroyMethod = "close")
  public DruidDataSource dataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setProxyFilters(Arrays.asList(statFilter()));
    return dataSource;
  }

  @Bean
  public Filter statFilter() {
    StatFilter filter = new StatFilter();
    filter.setSlowSqlMillis(1);
    filter.setLogSlowSql(true);
    filter.setMergeSql(true);
    return filter;
  }

  @Bean
  public ServletRegistrationBean<StatViewServlet> servletRegistrationBean() {
    return new ServletRegistrationBean<>(new StatViewServlet(),
        "/druid/*");
  }

}
