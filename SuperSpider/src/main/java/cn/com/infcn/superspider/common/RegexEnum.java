/**
 * 
 */
package cn.com.infcn.superspider.common;

import java.util.ArrayList;
import java.util.List;

import com.justme.superspider.util.RegexUtil;

/**
 * @author yangc
 *
 */
public enum RegexEnum {
	任意字符("任意字符","[\\s\\S]","0"),/*任意字符("任意字符","(.[\\s\\n\\r\\t]*)","0"),*/字母或数字("字母或数字",RegexUtil.anyChar,"0"),数字("数字",RegexUtil.num,"0"),空白("空白",RegexUtil.blank,"0"),双引号("双引号",RegexUtil.doubleQuotation,"0"),单引号("单引号",RegexUtil.singleQuotation,"0")
	,任意次("任意次",RegexUtil.zeroOrMore,"1"),零或一次("零或一次",RegexUtil.zeroOrOne,"1"),一次或更多次("一次或更多次",RegexUtil.oneOrMore,"1");
	private String value;//值
	private String name;//名称;
	private String type;//类型;
	private RegexEnum(){}
	private RegexEnum(String name,String value){
		this(name,value,null);
	}
	private RegexEnum(String name,String value,String type){
		this.name = name;
		this.value = value;
		this.type = type;
	}
	/**
	 * 根据类型获取regex值
	 * @param type
	 * @return
	 */
	public static String getValue(String name){
		if(name == null || "".equals(name))
			return name;
		for(RegexEnum regexEnum : RegexEnum.values()){
			if(regexEnum.name.equals(name)){
				return regexEnum.value;
			}
		}
		return name;
	}
	
	public static boolean isContain(String name){
		if(name == null || "".equals(name))
			return false;
		for(RegexEnum regexEnum : RegexEnum.values()){
			if(regexEnum.name.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public static List<RegexEnum> getRegexByType(String type){
		List<RegexEnum> regexList = new ArrayList<RegexEnum>();
		if(type == null || "".equals(type))
			return regexList;
		for(RegexEnum regexEnum : RegexEnum.values()){
			if(regexEnum.type.equals(type)){
				regexList.add(regexEnum);
			}
		}
		return regexList;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
