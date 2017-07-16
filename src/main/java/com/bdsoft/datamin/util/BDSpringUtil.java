package com.bdsoft.datamin.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring容器工具类 
 */
public class BDSpringUtil {

	private static String SPRING_XML = "classpath:spring/spring.xml";

	private static ApplicationContext SPRING_CONTEXT;

	private static boolean IS_INIT = Boolean.FALSE;

	// web方式初始化
	public static void init(ApplicationContext ctx) {
		SPRING_CONTEXT = ctx;
		IS_INIT = Boolean.TRUE;
	}

	// 非web方式初始化
	public static void init() {
		synchronized (BDSpringUtil.class) {
			if (!IS_INIT) {
				try {
					// 初始化日志服务
					BDLogUtil.init();

					// Spring容器
					SPRING_CONTEXT = new ClassPathXmlApplicationContext(SPRING_XML);
					IS_INIT = Boolean.TRUE;
				} catch (Exception e) {
					System.err.println("Spring加载失败：" + e.getMessage());
					System.err.println("程序退出");
					System.exit(1);
				}
			}
		}
	}

	public static boolean isInit() {
		return IS_INIT;
	}

	public static Object getBean(String bean) {
		return SPRING_CONTEXT.getBean(bean);
	}

	public static <T> T getBean(Class<T> t) {
		return SPRING_CONTEXT.getBean(t);
	}

}