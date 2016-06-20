package test.lisong.elastic.client;

import org.elasticsearch.action.get.GetResponse;
import org.junit.Test;

import test.lisong.elastic.utils.PrintUtils;

public class TestGet extends BaseTest {

	@Test
	public void testGetD() throws Exception {
		GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
		System.out.println(response);
		System.out.println(response.getSource());
		PrintUtils.showGetResponse(response);
	}
}
