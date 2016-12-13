/**
 * 
 */
package cn.com.infcn.superspider.utils;

import com.justme.superspider.url.UrlRuleChecker;

/**
 * @description 
 * @author WChao
 * @date   2016年5月31日 	下午3:32:32
 */
public class RuleUtil {
	public final static String regex_rule = "正则表达式";
	public final static String start_rule = "开始于";
	public final static String end_rule = "结束于";
	public final static String contains_rule = "包含";
	public final static String equal_rule = "等于";
	
	public static String getRuleType(String typeStr){
		if(regex_rule.equals(typeStr))
			return UrlRuleChecker.regex_rule;
		if(start_rule.equals(typeStr))
			return UrlRuleChecker.start_rule;
		if(end_rule.equals(typeStr))
			return UrlRuleChecker.end_rule;
		if(contains_rule.equals(typeStr))
			return UrlRuleChecker.contains_rule;
		if(equal_rule.equals(typeStr))
			return UrlRuleChecker.equal_rule;
		return null;
	}
}
