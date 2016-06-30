package test.lisong.elastic.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import test.lisong.elastic.utils.ESUtils;
import test.lisong.elastic.utils.PrintUtils;

public class TestSearch {

	@Test
	public void testSearchCourse() throws Exception {
		String custype = "construct";
		String industryno = "G01";
		String sortField = "_score";
		String key = "铁路局";
		QueryBuilder typeQuery = null;
			typeQuery = QueryBuilders.matchQuery("custype", custype);
		QueryBuilder induQuery = null;
			induQuery = QueryBuilders.matchQuery("industryno", industryno);
		
		SortBuilder sortBuilder = SortBuilders.scoreSort();;
			sortBuilder = SortBuilders.fieldSort(sortField).order(SortOrder.DESC);// 金额、公开状态正序
	
		Client client = ESUtils.getClient();
		QueryBuilder postFilter = null;
		if (typeQuery == null && induQuery == null) {
			postFilter = QueryBuilders.matchAllQuery();
		} else if (typeQuery == null && induQuery != null){
			postFilter = induQuery;
		} else if (typeQuery != null && induQuery == null) {
			postFilter = typeQuery;
		} else {
			postFilter = QueryBuilders.boolQuery().filter(typeQuery).filter(induQuery);
		}

		
		QueryBuilder negativeQuery = QueryBuilders.multiMatchQuery(key, "orgname.cn", "tag.cn", "orgname.en", "tag.en").boost(0.9f);
		QueryBuilder positiveQuery = QueryBuilders.multiMatchQuery(key, "custitle.cn", "custitle.en").boost(50.0f);
		
		
		QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(negativeQuery)
				.should(positiveQuery);
		int start = 0;
		int count = 10;
		//		QueryBuilder postFilter;
		SearchResponse resp = client.prepareSearch("courses").setTypes("index")
				.setQuery(queryBuilder )
				.setPostFilter(postFilter)
				.setFrom(start)
				.addSort(sortBuilder )
				.setSize(count)
				.addHighlightedField("custitle.cn")
				.addHighlightedField("orgname.cn")
				.addHighlightedField("cusnote.cn")
//				.addHighlightedField("custitle.en")
//				.addHighlightedField("orgname.en")
//				.addHighlightedField("cusnote.en")
				.execute()
				.actionGet();
		System.out.println(queryBuilder.toString());
		System.out.println(postFilter.toString());
		System.out.println(sortBuilder.toString());
		String[] highlightfields = new String[]{"custitle", "orgname", "cusnote"};
		PrintUtils.printSearchResponse(resp, highlightfields);
//		assertTrue(qtime > 0);
	}

	
}
		