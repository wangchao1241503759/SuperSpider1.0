package cn.com.infcn.superspider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author WChao
 * @date 2016年1月23日
 */
@Entity
@Table(name = "file_pubattribute_attribute")
@DynamicUpdate(true) @DynamicInsert(true)
public class FilePubArributeAttribute {
	
	private String id;
	private String filePubAttributeId;
	private String fileAttributeId;
	
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "PUB_ID",length = 32)
	public String getFilePubAttributeId() {
		return filePubAttributeId;
	}
	public void setFilePubAttributeId(String filePubAttributeId) {
		this.filePubAttributeId = filePubAttributeId;
	}
	@Column(name = "ATTRIBUTE_ID",length = 32)
	public String getFileAttributeId() {
		return fileAttributeId;
	}
	public void setFileAttributeId(String fileAttributeId) {
		this.fileAttributeId = fileAttributeId;
	}
}
