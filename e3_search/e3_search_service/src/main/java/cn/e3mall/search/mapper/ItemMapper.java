package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.search.pojo.Item;

public interface ItemMapper {
	
	List<Item> getAllItems();
	
	Item getItemById(long itemId);
}
