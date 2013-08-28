package org.duomn.naive.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 功能：用于在Spring的容器中获取bean<br>
 * 配置：该类同时提供给Business层及WEB层使用，配置在applicationContext.xml
 * @author Hu Qiang
 *
 */

public class SpringContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}
	
	/** 根据类型获取bean */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
	
	@SuppressWarnings("unchecked")
	/** 根据名称获取bean */
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}
	
	
}
