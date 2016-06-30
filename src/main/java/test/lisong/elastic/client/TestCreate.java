package test.lisong.elastic.client;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import test.lisong.elastic.utils.JsonUtil;
import test.lisong.elastic.utils.PrintUtils;
import test.lisong.elastic.utils.RandomUtils;

/**
 * @author 李嵩
 * 新增记录
 */
public class TestCreate extends BaseTest {
	
	@Test
	public void IndexWithBuilder() throws Exception {

		IndexResponse response = client.prepareIndex("twitter", "tweet", ""+RandomUtils.randomInt())
				.setSource(XContentFactory.jsonBuilder()
						.startObject()
						.field("user", "kimchy")
						.field("postDate", new Date())
						.field("message", "trying out Elasticsearch").endObject())
				
				.get();
		System.out.println(response);
		PrintUtils.showIndexResponse(response);
		assertTrue(response.isCreated());
	}
	
	@Test
	public void indexWithJSONString() throws Exception {
		String json = "{" +
		        "\"user\":\"kimchy2\"," +
		        "\"postDate\":\"2013-02-28\"," +
		        "\"message\":\"trying out Elasticsearch2\"" +
		    "}";

		IndexResponse response = client.prepareIndex("twitter", "tweet")
		        .setSource(json)
		        .get();
		
		System.out.println(response);
		PrintUtils.showIndexResponse(response);
		assertTrue(response.isCreated());
	}
	
	@Test
	public void indexWithJson() throws Exception {
		Map<String, String> data = new HashMap<>();
		data.put("user", "kimchy3");
		data.put("postDate", "2013-03-30");
		data.put("message", "trying out Elasticsearch 333");
		
		IndexResponse response = client.prepareIndex("twitter", "tweet")
		        .setSource(JsonUtil.toJson(data))
		        .get();
		PrintUtils.showIndexResponse(response);
		assertTrue(response.isCreated());
	}
	
	
}
