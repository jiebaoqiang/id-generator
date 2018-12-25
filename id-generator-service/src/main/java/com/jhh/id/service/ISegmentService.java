package com.jhh.id.service;

import com.jhh.id.entity.Segment;
import java.util.List;

/**
 * @author renbangjie
 */
public interface ISegmentService {

  /**
   * 更新数据库id分段表，并返回更新后的值
   * @param bizName 业务名称
   * @return com.jhh.id.entity.Segment
   */
  Segment incrementAndGet(String bizName);


  /**
   * 查询所有
   * @return
   */
  List<Segment> selectList();

}
