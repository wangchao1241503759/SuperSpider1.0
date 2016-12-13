package cn.com.infcn.superspider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import cn.com.infcn.superspider.utils.StringUtil;

/**
 * 
 * @author WChao
 * @date 2016年1月23日
 */
@Entity
@Table(name = "file_type")
@DynamicUpdate(true) @DynamicInsert(true)
public class FileType {
		
	private String fileTypeId;//文件类型Id;
	private String fileHeaderCode;//文件头信
	private String fileTypeName;//文件类型名称;
	private String fileTypeValue;//文件类型值;
	
	public FileType(){}
	public FileType(String fileTypeId){
		this.fileTypeId = fileTypeId;
	}
	@Id
	@Column(name = "ID", length = 32, nullable = false)
	public String getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	@Column(name = "FILE_TYPE_NAME",length = 50)
	public String getFileTypeName() {
		return fileTypeName;
	}
	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}
	@Column(name = "FILE_TYPE_VALUE",length = 50)
	public String getFileTypeValue() {
		return fileTypeValue;
	}
	public void setFileTypeValue(String fileTypeValue) {
		this.fileTypeValue = fileTypeValue;
	}
	@Column(name = "FILE_HEADER_CODE",length = 50)
	public String getFileHeaderCode() {
		return fileHeaderCode;
	}
	public void setFileHeaderCode(String fileHeaderCode) {
		this.fileHeaderCode = fileHeaderCode;
	}
	public static void main(String[] args) {
		for(int i =0 ;i<50;i++)
		{
			System.out.println(StringUtil.generateUUID());
		}
	}
}
