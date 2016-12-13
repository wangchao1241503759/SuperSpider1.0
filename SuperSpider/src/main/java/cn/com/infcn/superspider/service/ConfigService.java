/**
 * 
 */
package cn.com.infcn.superspider.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.infcn.superspider.model.FileAttribute;
import cn.com.infcn.superspider.model.TaskInfo;

/**
 * @author zyz
 *
 */
@Service
public class ConfigService {

	public List<TaskInfo> getTaskList(){
		List<TaskInfo> result = new ArrayList<TaskInfo>();
	
		result.add(new TaskInfo("发改委内网数据采集","数据库采集任务","发改委内网数据采集","张有志","2015-12-11","周末凌晨采集","从2015-12-11开始，每周六、周日24：00执行","等待"));
		result.add(new TaskInfo("青岛政务数据采集","WEB采集任务","青岛政务数据采集","张有志","2015-12-11","每天凌晨采集","从2015-12-11开始，每天24：00执行","执行中"));
		result.add(new TaskInfo("华能集团数据采集","文件采集任务","华能集团数据采集","张有志","2015-12-11","周末凌晨采集","从2015-12-11开始，每周六、周日24：00执行","执行中"));
		result.add(new TaskInfo("国家图书馆数据采集","FTP采集任务","国家图书馆数据采集","张有志","2015-12-11","每天凌晨采集","从2015-12-11开始，每天24：00执行","停止"));
		result.add(new TaskInfo("北京电力医院数据采集","文件采集任务","北京电力医院数据采集","张有志","2015-12-11","每天凌晨采集","从2015-12-11开始，每天24：00执行","等待"));
		
		return result;
	}
	
	public List<FileAttribute> getFileAttribute(){
		List<FileAttribute> result = new ArrayList<FileAttribute>();
		
		/*result.add(new FileAttribute("文件大小", "filesize", "int","*.*"));
		result.add(new FileAttribute("创建时间", "createtime", "date","*.*"));
		result.add(new FileAttribute("修改时间", "modifytime", "date","*.*"));
		result.add(new FileAttribute("标题", "title", "varchar(255)","*.word"));
		result.add(new FileAttribute("主题", "subject", "varchar(255)","*.word"));
		result.add(new FileAttribute("标记", "sign", "varchar(100)","*.word"));
		result.add(new FileAttribute("类别", "category", "varchar(50)","*.word"));
		result.add(new FileAttribute("备注", "memo", "varchar(255)","*.word"));
		result.add(new FileAttribute("作者", "author", "varchar(100)","*.word"));
		result.add(new FileAttribute("修订号", "revisenum", "varchar(100)","*.word"));
		result.add(new FileAttribute("版本号", "versionnum", "varchar(100)","*.word"));
		result.add(new FileAttribute("内容状态", "contentstate", "varchar(100)","*.word"));
		result.add(new FileAttribute("页码范围", "pagersize", "int","*.*"));
		result.add(new FileAttribute("字数", "wordtotal", "int","*.*"));
		result.add(new FileAttribute("行数", "rows", "int","*.word"));
		result.add(new FileAttribute("段落数", "paragraphcount", "int","*.word"));
		result.add(new FileAttribute("全文内容", "content", "blob","*.*"));*/
		
		return result;
	}
}
