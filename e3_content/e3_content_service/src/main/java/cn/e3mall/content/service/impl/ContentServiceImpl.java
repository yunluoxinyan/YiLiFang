package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;

	@Override
	public DataGridResult getContentList(long categoryId,int page,int rows) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		DataGridResult result = new DataGridResult();
		result.setRows(list);
		result.setTotal(total);
		return result;
	}

	@Override
	public E3Result addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		try {
			jedisClient.hdel("content", content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}

	@Override
	public E3Result updateContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		try {
			jedisClient.hdel("content", content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContent(String ids) {
		String[] str = ids.split(",");
		for (String id : str) {
			try {
				TbContent content = contentMapper.selectByPrimaryKey(Long.parseLong(id));
				jedisClient.hdel("content", content.getCategoryId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			contentMapper.deleteByPrimaryKey(Long.parseLong(id));
		}
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentList(long categoryId) {
		try {
			String json = jedisClient.hget("content", categoryId+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		try {
			jedisClient.hset("content", categoryId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
