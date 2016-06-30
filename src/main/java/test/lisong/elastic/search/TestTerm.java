package test.lisong.elastic.search;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.junit.Assert.*;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;

import test.lisong.elastic.client.BaseTest;
import test.lisong.elastic.utils.PrintUtils;

/**
 * @author 李嵩
 * 精确匹配查询
 */
public class TestTerm extends BaseTest {

	@Test
	public void testTermQuery() throws Exception {
		QueryBuilder qb = termQuery(
			    "user",    
			    "kimchy"   
			);
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
	
	@Test
	public void testTermsQuery() throws Exception {
		QueryBuilder qb = termsQuery("tags",    
				"拥抱", "父母", "逻辑"); 
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		System.out.println(qb.toString());
		PrintUtils.printSearchResponse(resp, null);
	}
	
	@Test
	public void testRangeQuery() throws Exception {
		QueryBuilder qb = rangeQuery("viewed")   
			    .from(50)                            
			    .to(1000)                             
			    .includeLower(true)                 
			    .includeUpper(false);  
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
	
	@Test
	public void testRangeQuery2() throws Exception {
		QueryBuilder qb = rangeQuery("viewed")   
			    .gt(1000)                            
			    .lte(2000);                         
			   
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
	
	@Test
	public void testExistsQuery() throws Exception {
		QueryBuilder qb = existsQuery("tags");   
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
	
	@Test
	public void testMissingQuery() throws Exception {
//		QueryBuilder qb = mustNotQuery("user")      
//	    .existence(true)                            
//	    .nullValue(true); 
	}
	
	@Test
	public void testPrefixQuery() throws Exception {
//		没查到，有问题
		QueryBuilder qb = prefixQuery(
			    "user",    
			    "Edsger"     
			);
		System.out.println(qb.toString());
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
	
	@Test
	public void testWildcardQuery() throws Exception {
//		没查到，有问题
		QueryBuilder qb = wildcardQuery("user", "E?sg*");
		System.out.println(qb.toString());
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
	
	@Test
	public void testRegexpQuery() throws Exception {
//		没查到，有问题
		QueryBuilder qb = regexpQuery(
			    "user",        
			    "M.*z"); 
		SearchResponse resp = client.prepareSearch("twitter").setTypes("tweet")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
}
