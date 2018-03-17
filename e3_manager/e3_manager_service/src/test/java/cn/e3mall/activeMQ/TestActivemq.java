package cn.e3mall.activeMQ;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActivemq {
	
	@Test
	public void testQueueMQ(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activeMQ.xml");
		JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
		Queue queue = (Queue)ac.getBean("queueDestination");
		jmsTemplate.send(queue, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("hello activeMQ");
				return textMessage;
			}
		});
	}
	
	@Test
	public void testTopicMQ(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activeMQ.xml");
		JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
		Topic topic = (Topic)ac.getBean("topicDestination");
		jmsTemplate.send(topic, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("hello activeMQ");
				return textMessage;
			}
		});
	}
}
