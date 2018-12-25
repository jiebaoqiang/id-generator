package com.jhh.id.generator;

import com.jhh.id.entity.Segment;

/**
 * @author renbangjie
 */
public interface IDataLoader {

  /**
   * 加载下一个号段相关信息
   */
  Segment load();

}
