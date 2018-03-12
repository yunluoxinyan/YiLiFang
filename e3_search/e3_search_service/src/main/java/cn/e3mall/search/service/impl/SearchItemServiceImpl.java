package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.search.dao.SearchItemDao;
import cn.e3mall.search.pojo.SearchResult;
import cn.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {
	
	@Autowired
	private SearchItemDao searchItemDao;
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public SearchResult searchItem(int page, String keywords,int rows) throws Exception {
		SolrQuery query = new SolrQuery();
		query.setQuery(keywords);
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		query.set("df", "item_keywords");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		SearchResult searchResult = searchItemDao.searchItem(query);
		long totalPages = (long) Math.ceil(((double)searchResult.getRecourdCount())/rows);
		searchResult.setTotalPages(totalPages);
		return searchResult;
	}

}
