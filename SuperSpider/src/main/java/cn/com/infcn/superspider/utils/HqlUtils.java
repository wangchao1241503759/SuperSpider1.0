/**
 * Copyright(c) 2015 Beijing INFCN Software Co.,Ltd.
 * @date：2015年12月2日
 */
package cn.com.infcn.superspider.utils;

import cn.com.infcn.superspider.pagemodel.PageHelper;


/**
 * @author lihf
 * @date 2015年12月2日
 */
public class HqlUtils {

  /**
   * @author lihf
   * @date 2015年11月27日 上午11:57:01
   * @param ph
   *          分页辅助类
   * @return
   */
  public static String orderHql(PageHelper ph) {
    String orderString = "";
    if (ph.getSort() != null && ph.getOrder() != null) {
      orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
    }
    return orderString;
  }
}
