package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.ItemService;

@Controller
public class ImportItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItems(){
		return itemService.importItems();
	}
}
