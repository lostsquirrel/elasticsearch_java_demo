package test.lisong.elastic.client;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Test;

public class TestMultGet extends BaseTest {

	@Test
	public void testMuiltGet() throws Exception {
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
			    .add("twitter", "tweet", "1")           
			    .add("twitter", "tweet", "2", "3", "4") 
			    .add("another", "type", "foo")          
			    .get();

			for (MultiGetItemResponse itemResponse : multiGetItemResponses) { 
			    GetResponse response = itemResponse.getResponse();
			    if (response != null && response.isExists()) {                      
			        String json = response.getSourceAsString(); 
			        System.out.println(json);
			    }
			}
	}
}
