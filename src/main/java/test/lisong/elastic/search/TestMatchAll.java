package test.lisong.elastic.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import test.lisong.elastic.client.BaseTest;
import test.lisong.elastic.utils.PrintUtils;

public class TestMatchAll extends BaseTest {

	@Test
	public void testName() throws Exception {
		QueryBuilder qb = QueryBuilders.matchAllQuery();
		
		SearchResponse resp = client.prepareSearch("courses").setTypes("index")
				.setQuery(qb).get();
		PrintUtils.printSearchResponse(resp, null);
	}
}
