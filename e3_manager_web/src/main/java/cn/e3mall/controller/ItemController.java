package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemId) {
		TbItem item = itemService.queryByItemId(itemId);
		return item;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public DataGridResult getItemList(int page,int rows) {
		DataGridResult list = itemService.getItemList(page, rows);
		return list;
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		return itemService.addItem(item,desc);	 
	}
	
	@RequestMapping("/item/desc/{itemId}")
	@ResponseBody
	public E3Result getItemDesc(@PathVariable long itemId){
		return itemService.getItemDesc(itemId);
	}
	
	@RequestMapping("/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item,String desc){
		return itemService.updateItem(item,desc);
	}
	
	@RequestMapping("/item/instock")
	@ResponseBody
	public E3Result instockItem(String[] ids){
		return itemService.instockItem(ids);
	}
	
	@RequestMapping("/item/reshelf")
	@ResponseBody
	public E3Result reshelfItem(String[] ids){
		return itemService.reshelfItem(ids);
	}
	
	@RequestMapping("/item/delete")
	@ResponseBody
	public E3Result deleteItem(String[] ids){
		return itemService.deleteItem(ids);
	}
}
