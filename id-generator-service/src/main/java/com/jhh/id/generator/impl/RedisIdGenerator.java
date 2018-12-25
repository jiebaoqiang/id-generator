package com.jhh.id.generator.impl;

import com.jhh.id.entity.Segment;
import com.jhh.id.generator.AbstractGenerator;
import com.jhh.id.generator.IDataLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 根据ID区间生成该区间所有的ID序列并保存在阻塞队列中
 *
 * @author renbangjie
 */
@Slf4j
public class RedisIdGenerator extends AbstractGenerator {

  public RedisIdGenerator(String bizName, double threshold,
      IDataLoader dataLoader,
      ExecutorService executorService,
      RedisTemplate<String, Long> redisTemplate) {
    super(bizName, threshold, dataLoader, executorService);
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Number nextId(long timeout, TimeUnit unit) {
    if (this.thresholdOverflow()) {
      this.loadData();
    }
    return opsForList().leftPop(timeout, unit);
  }


  /**
   * 加载下一个号段
   */
  public void loadData() {
    final String lockKey = this.buildKeyName(LOCK_PREFIX);
    if (getLock(lockKey)) {
      executorService.execute(() -> {
        try {
          if (!this.thresholdOverflow()) {
            return;
          }
          Segment segment = dataLoader.load();
          long start = (segment.getMaxId() + 1) - segment.getStep();
          Long[] ids = new Long[segment.getStep()];
          for (int i = 0; i < ids.length; i++) {
            ids[i] = start + i;
          }
          opsForList().rightPushAll(ids);
          opsForValue().set((long) segment.getStep());
        } finally {
          releaseLock(lockKey);
        }
      });
    }
  }


  /**
   * 判断是否可以加载数据
   */
  public boolean thresholdOverflow() {
    long size = opsForList().size();
    if (size <= 0) {
      return true;
    }
    Long step = opsForValue().get();
    double ratio = (double) (step - size) / step;
    return (size < step) && (ratio >= threshold);
  }

  /**
   * 获取锁
   */
  private boolean getLock(final String keyName) {
    RedisSerializer<String> redisSerializer = redisTemplate
        .getStringSerializer();
    return redisTemplate.execute((connection) -> {
      byte[] key = redisSerializer.serialize(keyName);
      byte[] value = redisSerializer.serialize("this is a lock");
      return connection
          .set(key, value, Expiration.from(10000, TimeUnit.MILLISECONDS),
              SetOption.ifAbsent());
    }, true);
  }

  /**
   * 释放锁
   */
  private void releaseLock(String lockKey) {
    if (!redisTemplate.delete(lockKey)) {
      redisTemplate.delete(lockKey);
    }
  }


  private BoundListOperations<String, Long> opsForList() {
    return redisTemplate.boundListOps(this.buildKeyName(ID_PREFIX));
  }


  private BoundValueOperations<String, Long> opsForValue() {
    return redisTemplate.boundValueOps(this.buildKeyName(STEP_PREFIX));
  }


  /**
   * 构造redis key
   */
  private String buildKeyName(String prefix) {
    StringBuilder key = new StringBuilder(prefix);
    key.append(".");
    key.append(bizName);
    return key.toString();
  }


  private static final String LOCK_PREFIX = "lock";

  private static final String ID_PREFIX = "id";

  private static final String STEP_PREFIX = "step";

  /**
   * 缓存操作类
   */
  private final RedisTemplate<String, Long> redisTemplate;

}
