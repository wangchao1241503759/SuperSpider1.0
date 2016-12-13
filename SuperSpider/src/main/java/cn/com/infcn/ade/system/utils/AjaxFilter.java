package cn.com.infcn.ade.system.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AjaxFilter implements Filter{
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest servletRequestt, ServletResponse servletResponse,FilterChain chain) throws IOException, ServletException {
		 HttpServletRequest request=(HttpServletRequest) servletRequestt;
		 HttpServletResponse response=(HttpServletResponse) servletResponse;
		 //String currentURL = request.getRequestURI();//取得根目录所对应的绝对路径:
		/* String baseURL = currentURL.substring(currentURL.indexOf("/"),currentURL.indexOf("/",1));
		 String targetURL = currentURL.substring(currentURL.indexOf("/", 1), currentURL.length());  //截取到当前文件名用于比较
*/		 
//		 Date currentDate = DateUtils.getCurrentDateDate();
//		 if(currentDate.compareTo(licenceDate) > 0){//注册license日期;
//			 if(!(currentURL.endsWith(".css") || currentURL.endsWith(".jpg"))){
//				 String target = "/WEB-INF/views/license/SuperSpiderRegisterFail.jsp";
//				 request.getRequestDispatcher(target).forward(request, response);
//				 return;
//			 }
//		 }else{
			 String ajaxSubmit = request.getHeader("X-Requested-With");
			 if(ajaxSubmit != null && ajaxSubmit.equals("XMLHttpRequest")){
				 if (request.getSession(false) == null) {
					 response.setHeader("sessionstatus", "timeout");
					 response.getWriter().print("sessionstatus");
					 return;
				 }
//			 }
		 }
		 chain.doFilter(servletRequestt, servletResponse);
	}

	public void destroy() {
		
	}
}
