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
 * @date   2016年2月23日 	上午10:29:40
 */
@Entity
@Table(name = "file_pub_attribute")
@DynamicUpdate(true) @DynamicInsert(true)
public class FilePubAttribute {
	private String pubAttributeId;//公用文件属性ID;
	private String attributeName;//属性名称;
	private String isDefault;//是否默认;
	private int sort;//排序字段;
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getPubAttributeId() {
		return pubAttributeId;
	}
	public void setPubAttributeId(String pubAttributeId) {
		this.pubAttributeId = pubAttributeId;
	}
	@Column(name = "ATTRIBUTE_NAME",length = 50)
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	@Column(name = "IS_DEFAULT",length = 2)
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	@Column(name = "SORT",columnDefinition="INT")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}
