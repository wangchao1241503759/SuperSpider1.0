package cn.com.infcn.superspider.utils;

/**
 * @author WChao
 * @date 2016年2月28日
 */
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringConfigTool implements ApplicationContextAware{

	  private static ApplicationContext applicationContext;//Spring应用上下文环境  
	   
	  /** 
	  * 实现ApplicationContextAware接口的回调方法，设置上下文环境    
	  * @param applicationContext 
	  * @throws BeansException 
	  */  
	  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {  
		  SpringConfigTool.applicationContext = applicationContext;  
	  }  
	   
	  /** 
	  * @return ApplicationContext 
	  */  
	  public static ApplicationContext getApplicationContext() {  
	    return applicationContext;  
	  }  
	   
	  /** 
	  * 获取对象    
	  * @param name 
	  * @return Object 一个以所给名字注册的bean的实例 
	  * @throws BeansException 
	  */  
	  public static Object getBean(String name) throws BeansException {  
	    return applicationContext.getBean(name);  
	  }  
	  /** 
	  * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true  
	  * @param name 
	  * @return boolean 
	  */  
	  public static boolean containsBean(String name) {  
	    return applicationContext.containsBean(name);  
	  }  
}
