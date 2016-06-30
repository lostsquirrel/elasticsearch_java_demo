package test.lisong.elastic.client;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Test;

/**
 * @author 李嵩
 * 测试从多个索引库或类型中，以ID获取数据
 */
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
