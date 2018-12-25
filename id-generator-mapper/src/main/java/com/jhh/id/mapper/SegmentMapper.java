package com.jhh.id.mapper;

import com.jhh.id.entity.Segment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author renbangjie
 */
@Mapper
public interface SegmentMapper {

  /**
   * 根据主键查询
   * @param bizName 业务名称
   * @return com.jhh.id.entity.Segment
   */
  Segment selectByPrimaryKey(String bizName);

  /**
   * 查询所有
   * @return
   */
  List<Segment> selectList();

  /**
   * 更新
   * @param segment id分段信息
   * @return int 受影响行数
   */
  int updateMaxId(Segment segment);

}
