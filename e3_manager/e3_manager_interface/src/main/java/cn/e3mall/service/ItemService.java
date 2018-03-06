package cn.e3mall.service;


import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.pojo.TbItem;

public interface ItemService {
	
	TbItem queryByItemId(long itemId);
	
	DataGridResult getItemList(int page,int rows);
}
