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
 * 
 * @author WChao
 * @date 2016年1月23日
 */
@Entity
@Table(name = "file_attribute")
@DynamicUpdate(true) @DynamicInsert(true)
public class FileAttribute {
	
	private String arributeId;
	private String attributeName;//属性名称;
	private String attributeValue;//属性值;
	private String typeId;//文件类型Id;
	public FileAttribute(){}
	
	public FileAttribute(String arributeId) {
		this.arributeId = arributeId;
	}
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getArributeId() {
		return arributeId;
	}
	public void setArributeId(String arributeId) {
		this.arributeId = arributeId;
	}
	
	@Column(name = "ATTRIBUTE_NAME",length = 50)
	public String getAttributeName() {
		return this.attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	@Column(name = "ATTRIBUTE_VALUE",length = 50)
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	@Column(name = "TYPE_ID",length = 32)
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
}
