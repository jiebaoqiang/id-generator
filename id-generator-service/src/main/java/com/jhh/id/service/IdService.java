package com.jhh.id.service;

public interface IdService {

  /**
   * 获取下一个ID
   */
  Number nextId(String bizName);


  void refresh();


}
