package test.lisong.elastic.client;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.junit.Test;

import test.lisong.elastic.utils.PrintUtils;

public class TestUpdate extends BaseTest {

	
	@Test
	public void testUpdateRequest() throws Exception {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("twitter");
		updateRequest.type("tweet");
		updateRequest.id("1");
		updateRequest.doc(XContentFactory.jsonBuilder()
		        .startObject()
		            .field("age", "23")
		        .endObject());
		UpdateResponse resp = client.update(updateRequest).get();
		PrintUtils.showUpdateResponse(resp);
	}
	
	@Test
	public void testUpdateScript() throws Exception {
		UpdateResponse resp = client.prepareUpdate("ttl", "doc", "1")
        .setScript(new Script("ctx._source.gender = \"male\""  , ScriptService.ScriptType.INLINE, null, null))
        .get();
		
		PrintUtils.showUpdateResponse(resp);
	}
	
	@Test
	public void testPrepareUpdate() throws Exception {
		UpdateResponse resp = client.prepareUpdate("ttl", "doc", "1")
        .setDoc(XContentFactory.jsonBuilder()               
            .startObject()
                .field("gender", "male")
            .endObject())
        .get();
		
		PrintUtils.showUpdateResponse(resp);
	}
	
	@Test
	public void testUpset() throws Exception {
		IndexRequest indexRequest = new IndexRequest("ttl", "doc", "2")
		        .source(XContentFactory.jsonBuilder()
		            .startObject()
		                .field("name", "Joe Smith")
		                .field("gender", "male")
		            .endObject());
		UpdateRequest updateRequest = new UpdateRequest("ttl", "doc", "2")
		        .doc(XContentFactory.jsonBuilder()
		            .startObject()
		                .field("gender", "male")
		            .endObject())
		        .upsert(indexRequest);              
		UpdateResponse resp = client.update(updateRequest).get();
		
		PrintUtils.showUpdateResponse(resp);
	}
}
