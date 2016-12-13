/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年6月6日
 */
package cn.com.infcn.superspider.utils;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import cn.com.infcn.superspider.common.Constant;

import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.url.UrlRuleChecker;
import com.justme.superspider.xml.Rule;
import com.justme.superspider.xml.Rules;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;

/**
 * @author lihf
 * @date 2016年6月6日
 */
public class WebUtil
{

	public static String convertRuleTypeNameToEn(String ruleTypeCn)
	{
		if(ruleTypeCn.equalsIgnoreCase("开始于"))
		{
			return UrlRuleChecker.start_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("结束于"))
		{
			return UrlRuleChecker.end_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("正则表达式"))
		{
			return UrlRuleChecker.regex_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("包含"))
		{
			return UrlRuleChecker.contains_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("等于"))
		{
			return UrlRuleChecker.equal_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("不开始于"))
		{
			return UrlRuleChecker.not_start_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("不结束于"))
		{
			return UrlRuleChecker.not_end_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("不包含"))
		{
			return UrlRuleChecker.not_contains_rule;
		}
		if(ruleTypeCn.equalsIgnoreCase("不等于"))
		{
			return UrlRuleChecker.not_equal_rule;
		}
		
		return "";
	}
	
	public static String convertRuleTypeNameToCn(String ruleTypeEn)
	{
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.start_rule))
		{
			return "开始于";
		}
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.end_rule))
		{
			return "结束于";
		}
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.regex_rule))
		{
			return "正则表达式";
		}
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.contains_rule))
		{
			return "包含";
		}
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.equal_rule))
		{
			return "等于";
		}
		
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.not_equal_rule))
		{
			return "不等于";
		}
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.not_start_rule))
		{
			return "不开始于";
		}
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.not_end_rule))
		{
			return "不结束于";
		}
		if(ruleTypeEn.equalsIgnoreCase(UrlRuleChecker.not_contains_rule))
		{
			return "不包含";
		}
		
		return "";
	}
	
	/**
	 * 转换html转义字符串
	 * @author lihf
	 * @date 2016年6月8日	上午9:07:45
	 * @param htmlEncode
	 * @return
	 */
	public static String getHtmlDecode(String htmlEncode)
	{
		return StringEscapeUtils.unescapeHtml3(htmlEncode);
	}
	
	/**
	 * 替换字符串
	 * @author lihf
	 * @date 2016年8月25日	上午9:02:45
	 * @param htmlEncode
	 * @return
	 */
	public static String replaceHtmlDecode(String htmlEncode)
	{
		return htmlEncode.replaceAll("&lt;","<" ).replaceAll("&gt;",">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	}
	
	/**
	 * 根据关联字段获取级别
	 * @param machField
	 * @return
	 */
	public static Integer getLevel(String machField, Site site)
	{
		String match_fields = (String) site.getOption(Constant.MATCH_FIELDS);
		if (machField == null || "".equals(machField)|| StringUtil.isEmpty(match_fields))
			return null;
		JSONObject jsonObject = JSONObject.parseObject(match_fields);
		for (String key : jsonObject.keySet())
		{
			if (machField.equals(jsonObject.getString(key)))
			{
				return Integer.parseInt(key);
			}
		}
		return null;
	}
	/**
	 * 根据级别获取目标Target;
	 * @param task
	 * @param level
	 * @return
	 */
	public static Target getTarget(Site site ,Integer level){
		List<Target> targets = site.getTargets().getTarget();
		for(Target target : targets){
			if(target.getId().equals(level.toString()))
				return target;
		}
		return null;
	}
	/**
	 * 检查Url是否符合目标规则;
	 * @param target
	 * @param newUrls
	 * @return
	 */
	public static boolean checkRule(Target target,Collection<Object> newUrls){
		Rules rules = target.getUrlRules();
		if(rules != null)
		{
			for(Object url : newUrls){
				Rule tgtRule = UrlRuleChecker.check(url.toString(), rules.getRule(), rules.getPolicy());
				if (tgtRule != null){
					return true;
				}
			}
		}else{
			return true;
		}
		return false;
	}
}
