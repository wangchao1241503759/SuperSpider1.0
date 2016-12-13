package com.superspider.junit;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.unitils.spring.annotation.SpringBeanByType;

import cn.com.infcn.ade.system.service.UserService;


@ContextConfiguration(locations = { "/applicationContext.xml" })
public class SuperSpiderTest extends AbstractJUnit4SpringContextTests {
	
	@SpringBeanByType
	private UserService userService;

	@Test
	public void quartzTest() throws Exception {
		/*String[] names=applicationContext.getBeanDefinitionNames();
		for(String s:names){
			System.out.println(s);
		}*/
		System.out.println(userService.getAll().size());
	}
}