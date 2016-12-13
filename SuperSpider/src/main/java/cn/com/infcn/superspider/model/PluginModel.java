/**
 * 
 */
package cn.com.infcn.superspider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @description 
 * @author WChao
 * @date   2015年12月29日 	下午6:50:18
 */
@Entity
@Table(name = "plugin")
@DynamicUpdate(true) @DynamicInsert(true)
public class PluginModel {
	
	private String id;//主键id;
	private String name;//插件名称;
	private String desc;//插件说明;
	private String version;//插件版本;
	private String type;//插件类型;
	private String enable;//是否开启;

	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "NAME",length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "DESCRITION",length = 200)
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Column(name = "VERSION",length = 32)
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	@Column(name = "TYPE",length = 20)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "ENABLE",length = 1)
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	
}
