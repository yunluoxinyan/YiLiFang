package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCatServiceImpl implements ContentCatService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<TreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<TreeNode> treeNodes = new ArrayList<>();
		for (TbContentCategory TbContentCategory : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(TbContentCategory.getId());
			treeNode.setText(TbContentCategory.getName());
			treeNode.setState(TbContentCategory.getIsParent()?"closed":"open");
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setParentId(parentId);
		category.setName(name);
		category.setStatus(1);
		category.setSortOrder(1);
		category.setIsParent(false);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		contentCategoryMapper.insert(category);
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!contentCategory.getIsParent()){
			contentCategory.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(contentCategory);
		}
		return E3Result.ok(category);
	}

	@Override
	public E3Result updateContentCategory(long id, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContentCategory(long id) {
		TbContentCategory child = contentCategoryMapper.selectByPrimaryKey(id);
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(child.getParentId());
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		if(list.size() == 1) {
			TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(child.getParentId());
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		deleteById(id);
		return E3Result.ok();
//		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
//		if(contentCategory.getIsParent()){
//			E3Result e3Result = new E3Result(100, null, null);
//			return e3Result;
//		} else {
//			TbContentCategory child = contentCategoryMapper.selectByPrimaryKey(id);
//			TbContentCategoryExample example = new TbContentCategoryExample();
//			Criteria criteria = example.createCriteria();
//			criteria.andParentIdEqualTo(child.getParentId());
//			List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
//			if(list.size() == 1){
//				TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(child.getParentId());
//				parent.setIsParent(false);
//				contentCategoryMapper.updateByPrimaryKey(parent);
//			}
//			contentCategoryMapper.deleteByPrimaryKey(id);
//			
//			return E3Result.ok();
//		}
	}

	private void deleteById(long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		if(list == null || list.size() == 0 ){
			contentCategoryMapper.deleteByPrimaryKey(id);
		} else {
			for (TbContentCategory tbContentCategory : list) {
				deleteById(tbContentCategory.getId());
			}
			contentCategoryMapper.deleteByPrimaryKey(id);
		}
	}

}
