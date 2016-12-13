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
 * @date   2015年12月29日 	下午6:50:34
 */
@Entity
@Table(name = "plugin_point")
@DynamicUpdate(true) @DynamicInsert(true)
public class PointModel {
	
	private String id;//主键id;
	private String pointName;//扩展点名;
	private String pointImpl;//扩展点实现类路径;
	private String sort;//扩展点排序;
	private String pluginId;//所属插件Id;
	
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "NAME",length = 30)
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	@Column(name = "IMPL",length = 200)
	public String getPointImpl() {
		return pointImpl;
	}
	public void setPointImpl(String pointImpl) {
		this.pointImpl = pointImpl;
	}
	@Column(name = "SORT",length = 5)
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	@Column(name = "PLUGIN_ID",length = 32)
	public String getPluginId() {
		return pluginId;
	}
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
}
