package test.lisong.elastic.client;

import java.util.Date;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import test.lisong.elastic.utils.PrintUtils;

public class TestIndex extends BaseTest {
	
	@Test
	public void IndexWithBuilder() throws Exception {

		IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
				.setSource(XContentFactory.jsonBuilder()
						.startObject()
						.field("user", "kimchy")
						.field("postDate", new Date())
						.field("message", "trying out Elasticsearch").endObject())
				.get();
		System.out.println(response);
		PrintUtils.showIndexResponse(response);
	}
	
	@Test
	public void indexWithJSONString() throws Exception {
		String json = "{" +
		        "\"user\":\"kimchy2\"," +
		        "\"postDate\":\"2013-01-30\"," +
		        "\"message\":\"trying out Elasticsearch2\"" +
		    "}";

		IndexResponse response = client.prepareIndex("twitter", "tweet")
		        .setSource(json)
		        .get();
		
		System.out.println(response);
		PrintUtils.showIndexResponse(response);
	}
	
	
}
