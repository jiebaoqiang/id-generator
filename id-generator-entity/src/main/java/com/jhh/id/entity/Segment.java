package com.jhh.id.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renbangjie
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Segment implements Serializable {

  private static final long serialVersionUID = 4685905356142570585L;
  /**
   * 业务名称
   */
  private String bizName;

  /**
   * 当前已生成的ID的最大值
   */
  private Long maxId;

  /**
   * ID生成服务一次加载到内存中的号段长度
   */
  private Integer step;

  /**
   * 0:趋势递增非单调递增，具有性能优势，推荐类型；
   * 1:保证单调递增，但有一定性能损失
   */
  private Integer type;

  /**
   * 描述
   */
  private String description;

  /**
   * 创建日期
   */
  private Date createDate;

  /**
   * 更新日期
   */
  private Date updateDate;

}
