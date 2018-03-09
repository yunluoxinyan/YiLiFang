package cn.e3mall.service;


import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;

public interface ItemService {
	
	TbItem queryByItemId(long itemId);
	
	DataGridResult getItemList(int page,int rows);

	E3Result addItem(TbItem item, String desc);

	E3Result getItemDesc(long itemId);

	E3Result updateItem(TbItem item, String desc);

	E3Result instockItem(String[] ids);

	E3Result reshelfItem(String[] ids);

	E3Result deleteItem(String[] ids);
}
