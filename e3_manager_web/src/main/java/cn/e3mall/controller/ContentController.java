package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public DataGridResult getContentList(long categoryId,int page,int rows){
		return contentService.getContentList(categoryId, page, rows);
	}
	
	@RequestMapping(value = "/content/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result saveContent(TbContent content){
		return contentService.addContent(content);
	}
	
	@RequestMapping(value = "/content/edit",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateContent(TbContent content){
		return contentService.updateContent(content);
	}
	
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContent(String ids){
		return contentService.deleteContent(ids);
	}
}
