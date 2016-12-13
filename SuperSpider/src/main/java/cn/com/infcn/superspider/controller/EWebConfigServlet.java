/**
 * 
 */
package cn.com.infcn.superspider.controller;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eweb4j.config.EWeb4JConfig;

/**
 * @description 
 * @author WChao
 * @date   2016年3月22日 	下午12:13:20
 */
public class EWebConfigServlet extends HttpServlet implements Servlet {
	
	Logger logger = Logger.getLogger(EWebConfigServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig conf) throws ServletException
	{
		//启动Eweb4j的xml模块;
		String err = EWeb4JConfig.start("eweb4j-start-config.xml");
		if (err != null)
		logger.error(err);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{

	}
}
