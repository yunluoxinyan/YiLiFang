package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class jedisTest {
	
	@Test
	public void Testjedis(){
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		jedis.set("age", "18");
		String age = jedis.get("age");
		System.out.println(age);
		jedis.close();
	}
	
	@Test
	public void TestjedisPool(){
		JedisPool jedisPool = new JedisPool("192.168.25.128", 6379);
		Jedis jedis = jedisPool.getResource();
		String age = jedis.get("age");
		System.out.println(age);
		jedis.close();
		jedisPool.close();
	}
	
	@Test
	public void TestjedisCluster(){
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("nihao", "hanmeimei");
		System.out.println(cluster.get("nihao"));
		cluster.close();
	}
	
	@Test
	public void testJedisClientPool(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient client = ac.getBean(JedisClient.class);
		System.out.println(client.get("world"));
	}
}
