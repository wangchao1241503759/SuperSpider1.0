/**
 * Copyright(c) 2015 Beijing INFCN Software Co.,Ltd.
 * @date：2015年11月27日
 */
package cn.com.infcn.superspider.utils;

import java.util.UUID;

/**
 * UUID辅助类.
 * 
 * @author lihf
 * @date 2015年11月27日
 */
public class UUIDCreater {

  /**
   * 创建UUID.
   * 
   * @author lihf
   * @date 2015年11月27日 下午2:55:38
   * @return
   */
  public static String getUUID() {
    String uuid = UUID.randomUUID().toString();
    uuid = uuid.replace("-", "");
    return uuid;
  }
}
