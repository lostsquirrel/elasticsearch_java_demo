package test.lisong.elastic.client;

import org.elasticsearch.action.delete.DeleteResponse;
import org.junit.Test;

import test.lisong.elastic.utils.PrintUtils;

public class TestDelete extends BaseTest {

	@Test
	public void testDelete() throws Exception {
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();
		
		PrintUtils.showDelResponse(response);
	}
}
