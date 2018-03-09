package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCatService;

@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCatService contentCatService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<TreeNode> getContentCatList(@RequestParam(name="id",defaultValue="0")long parentId){
		return contentCatService.getContentCatList(parentId);
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContentCat(long parentId,String name){
		return contentCatService.addContentCategory(parentId, name);
	}
	
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateContentCat(long id,String name){
		return contentCatService.updateContentCategory(id, name);
	}
	
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result deleteContentCat(long id){
		return contentCatService.deleteContentCategory(id);
	}
}
