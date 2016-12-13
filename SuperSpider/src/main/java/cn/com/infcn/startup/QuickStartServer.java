package cn.com.infcn.startup;

import java.util.ResourceBundle;

import org.eclipse.jetty.server.Server;
import org.eweb4j.config.EWeb4JConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.infcn.startup.jetty.JettyFactory;
import cn.com.infcn.startup.jetty.Profiles;



/**
 * 使用Jetty运行调试Web应用, 在Console输入回车快速重新加载应用.
 * 
 * @author WChao
 */
public class QuickStartServer {

	public static final String CONTEXT = "/SuperSpider";
	public static final String[] TLD_JAR_NAMES = new String[] {"spring-webmvc", "shiro-web"};
	public static Logger logger = LoggerFactory.getLogger(QuickStartServer.class);
	public static void main(String[] args) throws Exception {
		
		//设定Spring的profile
		Profiles.setProfileAsSystemProperty(Profiles.DEVELOPMENT);
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		//启动Eweb4j的xml模块;
		String err = EWeb4JConfig.start("eweb4j-start-config.xml");
		
		if (err != null)
	        throw new Exception(err);
		
		// 启动Jetty
		Server server = JettyFactory.createServerInSource(Integer.parseInt(bundle.getString("serverPort")), CONTEXT);
		JettyFactory.setTldJarNames(server, TLD_JAR_NAMES);

		try {
			server.start();
			
			logger.info("[INFO] 服务启动  http://localhost:" + bundle.getString("serverPort") + CONTEXT + bundle.getString("adminPath"));
			logger.info("[HINT] 按回车迅速重启");

			//等待用户输入回车重载应用.
			while (true) {
				char c = (char) System.in.read();
				if (c == '\n') {
					JettyFactory.reloadContext(server);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
