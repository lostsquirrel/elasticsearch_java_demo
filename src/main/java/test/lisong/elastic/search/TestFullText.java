package test.lisong.elastic.search;

import static org.junit.Assert.*;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.*;
import org.junit.Test;

import test.lisong.elastic.client.BaseTest;
import test.lisong.elastic.utils.PrintUtils;

public class TestFullText extends BaseTest {

	/**
	 * 单个分词字段查询
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testMatchQuery() throws Exception {
		QueryBuilder qb = matchQuery(
			    "user",                  
			    "kimchy elasticsearch"   
			);
		
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb)
				.addHighlightedField("user")
				.get();
		PrintUtils.printSearchResponse(resp, new String[]{"user"});
	}
	
	/**
	 * 多个分词字段查询
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testMultiMatchQuery() throws Exception {
		QueryBuilder qb = multiMatchQuery(
			    "kimchy elasticsearch", 
			    "user", "message"       
			);
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb)
				.addHighlightedField("user")
				.addHighlightedField("message")
				.get();
		PrintUtils.printSearchResponse(resp, new String[]{"user", "message"});
	}
	
	/**
	 * 用于 包含停词的查询， 具体还没太看明白
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testCommonTermsQuery() throws Exception {
		QueryBuilder qb = commonTermsQuery("user",    
                "kimchy");
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb)
				.addHighlightedField("user")
				.get();
		PrintUtils.printSearchResponse(resp, new String[]{"user"});
	}
}
