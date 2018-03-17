package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemDescExample.Criteria;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate JmsTemplate;
	
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO_EXPIRE}")
	private int ITEM_INFO_EXPIRE;

	@Override
	public TbItem queryByItemId(long itemId) {
		try {
			String json = jedisClient.get("ITEM_INFO_PRE:" + itemId + ":BASE");
			if(StringUtils.isNotBlank(json)){
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			jedisClient.set("ITEM_INFO_PRE:" + itemId + ":BASE", JsonUtils.objectToJson(item));
			jedisClient.expire("ITEM_INFO_PRE:" + itemId + ":BASE",ITEM_INFO_EXPIRE );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public DataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> info = new PageInfo<>(list);
		long total = info.getTotal();
		DataGridResult result = new DataGridResult();
		result.setRows(list);
		result.setTotal(total);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//'商品状态，1-正常，2-下架，3-删除',
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				JmsTemplate.send(topicDestination,new MessageCreator() {
					
					@Override
					public Message createMessage(Session session) throws JMSException {
						TextMessage message = session.createTextMessage(""+itemId);
						return message;
					}
				});
			}
		}).start();
		return E3Result.ok();
	}

	@Override
	public E3Result getItemDesc(long itemId) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		E3Result result = E3Result.ok(itemDesc);
		return result;
	}

	@Override
	public E3Result updateItem(TbItem item, String desc) {
		item.setUpdated(new Date());
//		item.setStatus((byte) 1);
		itemMapper.updateByPrimaryKeySelective(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		return E3Result.ok();
	}

	@Override
	public E3Result instockItem(String[] ids) {
		TbItem item = new TbItem();
		for (String id : ids) {
			item.setId(Long.parseLong(id));
			item.setStatus((byte) 2);
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return E3Result.ok();
	}

	@Override
	public E3Result reshelfItem(String[] ids) {
		TbItem item = new TbItem();
		for (String id : ids) {
			item.setId(Long.parseLong(id));
			item.setStatus((byte) 1);
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return E3Result.ok();
	}

	@Override
	public E3Result deleteItem(String[] ids) {
		TbItem item = new TbItem();
		for (String id : ids) {
			item.setId(Long.parseLong(id));
			item.setStatus((byte) 3);
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return E3Result.ok();
	}

	@Override
	public TbItemDesc queryByItemDescId(long itemId) {
		try {
			String json = jedisClient.get("ITEM_INFO_PRE:" + itemId + ":DESC");
			if(StringUtils.isNotBlank(json)){
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			jedisClient.set("ITEM_INFO_PRE:" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			jedisClient.expire("ITEM_INFO_PRE:" + itemId + ":DESC",ITEM_INFO_EXPIRE );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
