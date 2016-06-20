package test.lisong.elastic.client;

import org.elasticsearch.client.Client;
import org.junit.Before;

import test.lisong.elastic.utils.ESUtils;

public class BaseTest {

	protected Client client;

	@Before
	public void init() {
		client = ESUtils.getClient();
	}
}
