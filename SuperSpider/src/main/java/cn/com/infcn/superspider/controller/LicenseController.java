/**
 * 
 */
package cn.com.infcn.superspider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.superspider.pagemodel.Json;
import cn.com.infcn.superspider.pagemodel.LicenseParam;
import cn.com.infcn.superspider.service.CheckHandlerI;
/**
 * @description 
 * @author WChao
 * @date   2016年3月28日 	下午1:57:19
 */
@Controller
@RequestMapping("license")
public class LicenseController extends BaseController {
	
	@Autowired
	private CheckHandlerI checkHandler;

	@RequestMapping(value="fail",method = RequestMethod.GET)
	public String licenceFail(){
		
		return "license/SuperSpiderRegisterFail";
	}
	
	
	/**
	 * 检测是否过期
	 * @author lihf
	 * @date 2016年4月21日	下午5:26:55
	 * @return
	 */
	@RequestMapping("/checkLicense")
	@ResponseBody
	public Json checkLicense()
	{
		Json json = new Json();
		try
        {
	        checkHandler.checkExpireDate();
	        json.setSuccess(true);
	        json.setMsg("注册信息有效！");
        }
        catch (Exception e)
        {
        	json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/**
	 * 检测是否过期
	 * @author lihf
	 * @date 2016年4月21日	下午5:26:55
	 * @return
	 */
	@RequestMapping("/checkDbTypeLicense")
	@ResponseBody
	public Json checkDbTypeLicense(String dbType,String operation)
	{
		Json json = new Json();
		try
        {
			LicenseParam licenseParam = new LicenseParam();
			licenseParam.setDbType(dbType);
			licenseParam.setTaskType("db");
			licenseParam.setOperation(operation);
			licenseParam.setScope("select");
	        checkHandler.checkDB(licenseParam);
	        json.setSuccess(true);
	        json.setMsg("注册信息有效！");
        }
        catch (Exception e)
        {
        	json.setMsg(e.getMessage());
		}
		return json;
	}
}
