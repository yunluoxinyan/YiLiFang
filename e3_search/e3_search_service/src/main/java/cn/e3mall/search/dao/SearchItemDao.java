package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.search.pojo.Item;
import cn.e3mall.search.pojo.SearchResult;

@Repository
public class SearchItemDao {
	
	@Autowired
	private SolrServer solrServer;

	public SearchResult searchItem(SolrQuery query) throws Exception{
		QueryResponse response = solrServer.query(query);
		SolrDocumentList documentList = response.getResults();
		long numFound = documentList.getNumFound();
		List<Item> items = new ArrayList<>();
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		for (SolrDocument solrDocument : documentList) {
			Item item = new Item();
			item.setId((String) solrDocument.get("id"));
			String title = "";
			List<String> list = map.get(solrDocument.get("id")).get("item_title");
			if(list != null && list.size() > 0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			item.setPrice((long) solrDocument.get("item_price"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			items.add(item);
		}
		SearchResult result = new SearchResult();
		result.setItemList(items);
		result.setRecourdCount(numFound);
		return result;
	}

}
