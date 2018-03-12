package cn.e3mall.search.service;

import cn.e3mall.search.pojo.SearchResult;

public interface SearchItemService {
	
	SearchResult searchItem(int page, String keywords, int rows) throws Exception;
}
