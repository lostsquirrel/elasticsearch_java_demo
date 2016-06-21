package test.lisong.elastic.client;

import java.util.Date;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

public class TestJsonBuilder {

	@Test
	public void testObject() throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder()
			    .startObject()
			        .field("user", "kimchy")
			        .field("postDate", new Date())
			        .field("message", "trying out Elasticsearch")
			    .endObject();
		String json = builder.string();
		System.out.println(json);
	}
	
	@Test
	public void testArray() throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().startArray()
				.startObject()
				.field("user", "kimchy")
		        .field("postDate", new Date())
		        .field("message", "trying out Elasticsearch")
			    .endObject()
			    .startObject()
			    .field("user", "kimchy2")
			    .field("postDate", new Date())
			    .field("message", "trying out Elasticsearch2")
			    .endObject()
				.endArray();
		String json = builder.string();
		System.out.println(json);
	}
}
