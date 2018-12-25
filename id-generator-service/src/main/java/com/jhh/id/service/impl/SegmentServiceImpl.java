package com.jhh.id.service.impl;

import com.jhh.id.common.exception.BizException;
import com.jhh.id.entity.Segment;
import com.jhh.id.mapper.SegmentMapper;
import com.jhh.id.service.ISegmentService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SegmentServiceImpl implements ISegmentService {

  @Transactional
  @Override
  public Segment incrementAndGet(String bizName) {
    Segment segment = new Segment();
    segment.setBizName(bizName);
    segment.setUpdateDate(new Date());
    if (segmentMapper.updateMaxId(segment) != 1) {
      throw new BizException("更新ID分段表MaxID失败.");
    }
    return segmentMapper.selectByPrimaryKey(bizName);
  }

  @Override
  public List<Segment> selectList() {
    return segmentMapper.selectList();
  }

  @Autowired
  private SegmentMapper segmentMapper;

}
