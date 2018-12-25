package com.jhh.id.generator.impl;

import com.jhh.id.common.exception.BizException;
import com.jhh.id.entity.Segment;
import com.jhh.id.generator.AbstractGenerator;
import com.jhh.id.generator.IDataLoader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 根据ID区间生成该区间所有的ID序列并保存在阻塞队列中
 *
 * @author renbangjie
 */
@Slf4j
public class MemoryIdGenerator extends AbstractGenerator {

  public MemoryIdGenerator(String bizName, double threshold,
      IDataLoader dataLoader,
      ExecutorService executorService) {
    super(bizName, threshold, dataLoader, executorService);
  }

  @Override
  public Number nextId(long timeout, TimeUnit unit)
      throws InterruptedException {
    if (this.thresholdOverflow()) {
      this.loadData();
    }
    return blockingQueue.poll(timeout, unit);
  }

  /**
   * 加载下一个号段
   */
  public void loadData() {
    if (!running) {
      synchronized (this) {
        if (!running) {
          this.running = true;
          executorService.execute(() -> {
            Segment segment = dataLoader.load();
            if (segment == null) {
              return;
            }
            long start = segment.getMaxId() - segment.getStep() + 1;
            for (long id = start; id <= segment.getMaxId(); id++) {
              if (!blockingQueue.offer(id)) {
                throw new BizException("id放入队列失败.");
              }
            }
            totalSize = blockingQueue.size();
            running = false;
          });
        }
      }
    }
  }

  /**
   * 判断是否可以加载数据
   */
  public boolean thresholdOverflow() {
    double used = this.totalSize - blockingQueue.size();
    double ratio = used / this.totalSize;
    return blockingQueue.isEmpty() || (ratio >= threshold && !this.running);
  }


  /**
   * 一次加载完成后队列的总长度, 只有加载时才会改变 totalSize = segment.step + 队列当前大小
   */
  private volatile double totalSize = -1d;

  /**
   * id队列
   */
  protected BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue<>();
}
