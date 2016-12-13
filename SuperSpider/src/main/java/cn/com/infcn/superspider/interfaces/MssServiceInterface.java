package cn.com.infcn.superspider.interfaces;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.infcn.ade.common.controller.BaseController;
import cn.com.infcn.ade.system.service.UserService;
import cn.com.infcn.ade.system.utils.FormAuthenticationCaptchaFilter;
import cn.com.infcn.ade.system.utils.UsernamePasswordCaptchaToken;
import cn.com.infcn.superspider.utils.DESUtils;

/**
 * 元数据仓储接口服务;
 * @description 
 * @author WChao
 * @date   2016年3月15日 	下午4:17:38
 */
@Controller
@RequestMapping(value = "{mssPath}")
public class MssServiceInterface extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private FormAuthenticationCaptchaFilter myCaptchaFilter;
	
	private String secretKey = "1111111122222222infcn123";
	/**
	 * 任务管理页面
	 * @throws IOException 
	 */
	@RequestMapping(value="authorize",method = RequestMethod.GET)
	public void authorize(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Subject subject = SecurityUtils.getSubject();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(username == null || "".equals(username) || password == null || "".equals(password)){
			WebUtils.issueRedirect(request, response, "/mss/error/403", null, true);
			return;
		}
		try{
			if (!subject.isAuthenticated()) {
				password = DESUtils.decrypt(password, secretKey);
	            UsernamePasswordCaptchaToken mytoken = new UsernamePasswordCaptchaToken(username,password);
	            subject.login(mytoken);//登录
			}
			WebUtils.issueRedirect(request, response, "/admin", null, true);
		}catch(Exception e){
		    WebUtils.issueRedirect(request, response, "/mss/error/403", null, true);
			e.printStackTrace();
		}
	}
	@RequestMapping(value="error/403",method = RequestMethod.GET)
	public String error403(){
		return "error/403";
	}
}
