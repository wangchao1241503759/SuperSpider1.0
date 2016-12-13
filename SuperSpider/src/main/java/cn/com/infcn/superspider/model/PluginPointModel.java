/**
 * 
 */
package cn.com.infcn.superspider.model;

import java.util.List;

/**
 * @description 
 * @author WChao
 * @date   2015年12月29日 	下午8:13:17
 */
public class PluginPointModel {
	private String id;//主键id;
	private String name;//插件名称;
	private String desc;//插件说明;
	private String version;//插件版本;
	private String type;//插件类型;
	private String enable;//是否开启;
	private List<PointModel> points;//插件扩展点;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public List<PointModel> getPoints() {
		return points;
	}
	public void setPoints(List<PointModel> points) {
		this.points = points;
	}
}
