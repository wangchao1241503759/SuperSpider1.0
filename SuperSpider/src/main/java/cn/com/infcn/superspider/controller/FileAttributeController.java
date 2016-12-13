/**
 * 
 */
package cn.com.infcn.superspider.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.ade.common.persistence.Page;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.pagemodel.FileTypeAttribute;
import cn.com.infcn.superspider.service.impl.FileAttributeServiceImpl;

/**
 * @description 
 * @author WChao
 * @date   2016年1月25日 	下午3:14:38
 */
@Controller
@RequestMapping("fileAttributeController")
public class FileAttributeController extends BaseController{
	
	@Autowired
	private FileAttributeServiceImpl fileAttributeService;
	
	/**
	 * 文件属性提取集合页面;
	 * @return
	 */
	@RequestMapping(value="forFileAttributeList",method = RequestMethod.GET)
	public String forFileAttributeList(HttpServletRequest request) {
		String noDefaultIds = request.getParameter("noDefaultIds");
		if(noDefaultIds != null && !"".equals(noDefaultIds)){
			if(noDefaultIds.endsWith(","))
			noDefaultIds = noDefaultIds.substring(0,noDefaultIds.lastIndexOf(","));
		}
		request.setAttribute("noDefaultIds", noDefaultIds);
		return Constant.SUPERSPIDER+"/fileattribute/fileattribute_list";
	}
	
	@RequestMapping(value = "getNoDefaultAttributes",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> findAttributeByDefault(HttpServletRequest request){
		/*Page<Task> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = fileAttributeService.search(page, filters);
		if(page.getResult() != null && page.getResult().size() > 0){
			for(Task task : page.getResult()){
				if(task.getTaskPlanId() != null && !"".equals(task.getTaskPlanId())){
					SchedulePlan sp = schedulePlanService.get(task.getTaskPlanId());
					task.setPlanName(sp.getName());
					task.setPlanDescription(sp.getDescription());
				}
			}
		}
		return getEasyUIData(page);*/
		//String isDefault = request.getParameter("isDefault");
		Page<FileTypeAttribute> page = getPage(request);
		//List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		/*page = fileAttributeService.search(page, filters);*/
		List<FileTypeAttribute> fileAttributesList = fileAttributeService.findAttributeBySearchDefault("0");
		if(fileAttributesList != null && fileAttributesList.size() > 0){
			Iterator<FileTypeAttribute> it = fileAttributesList.iterator();
			String noDefaultIds = request.getParameter("noDefaultIds");
			if(noDefaultIds != null && !"".equals(noDefaultIds)){
				String[] ids = noDefaultIds.split(",");
				while(it.hasNext()){
					FileTypeAttribute fileTypeAttribute = it.next();
					for(String id : ids){
						if(fileTypeAttribute.getFileAttributeId().equals(id)){
							it.remove();
							break;
						}
					}
				}
			}
		}
		page.setResult(fileAttributesList);
		return getEasyUIData(page);
	}
}
