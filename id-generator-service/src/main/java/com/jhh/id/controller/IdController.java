package com.jhh.id.controller;

import com.jhh.id.common.exception.BizException;
import com.jhh.id.common.response.Result;
import com.jhh.id.common.utils.Assert;
import com.jhh.id.generator.impl.RedisIdGenerator;
import com.jhh.id.service.IdService;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author renbangjie
 */
@Slf4j
@RestController
@RequestMapping("id/v1")
public class IdController {

  private static final int MAX_COUNT = 10000;

  @ApiOperation("获取单个ID")
  @GetMapping("{bizName}")
  public Result<Long> get(@PathVariable String bizName) {
    Number nextId = idService.nextId(bizName);
    if (nextId == null) {
      throw new BizException("获取ID失败,阻塞超时");
    }
    return Result.success(nextId.longValue());
  }


  @ApiOperation("批量获取ID")
  @GetMapping("list/{bizName}/{count}")
  public Result<Long[]> list(@PathVariable String bizName,
      @PathVariable Integer count) {
    Assert.isTrue(count <= MAX_COUNT, "一次最多获取一万个Id");
    Long[] ids = new Long[count];
    Number nextId = null;
    for (int i = 0; i < count; i++) {
      nextId = idService.nextId(bizName);
      if (nextId == null) {
        throw new BizException("获取ID失败,阻塞超时");
      }
      ids[i] = nextId.longValue();
    }
    return Result.success(ids);
  }


  @ApiOperation("刷新缓存")
  @GetMapping("refresh")
  public Result<String> refresh() {
    idService.refresh();
    return Result.success();
  }

  @GetMapping("clear/{bizName}")
  public Result<Boolean> clear(@PathVariable String bizName) {
    return Result.success(redisTemplate.delete(buildKeyName(bizName)));
  }

  /**
   * 构造redis key
   */
  private String buildKeyName(String bizName) {
    StringBuilder key = new StringBuilder("id");
    key.append(".");
    key.append(RedisIdGenerator.class.getName());
    key.append(".");
    key.append(bizName);
    return key.toString();
  }


  @Autowired
  private IdService idService;

  @Resource(name = "redisTemplate")
  private RedisTemplate<String, Long> redisTemplate;

}
