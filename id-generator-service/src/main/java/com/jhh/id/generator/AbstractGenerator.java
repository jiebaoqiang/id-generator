package com.jhh.id.generator;

import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractGenerator implements IGenerator {

  /**
   * 号段名称
   */
  protected final String bizName;

  /**
   * 异步加载阈值
   */
  protected final double threshold;

  /**
   * 数据加载器
   */
  protected final IDataLoader dataLoader;


  /**
   * 线程池
   */
  protected final ExecutorService executorService;


  /**
   * 数据加载状态,false表示当前没有线程在加载数据
   */
  protected volatile boolean running = false;

}
