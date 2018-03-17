package cn.e3mall.ActiveMQ;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestActiveMq {
	@Test
	public void testQueueConsumer() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activeMQ.xml");
		//等待
		//System.in.read();
	}

}
