package test.lisong.elastic.client;

import static org.junit.Assert.*;

import org.elasticsearch.action.get.GetResponse;
import org.junit.Test;

import test.lisong.elastic.utils.PrintUtils;

/**
 * @author 李嵩
 * 获取单个记录
 */
public class TestGet extends BaseTest {

	/**
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 29, 2016
	 * 根据索引库，类型， ID 获取指定记录
	 */
	@Test
	public void testGetD() throws Exception {
		GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
		System.out.println(response);
		System.out.println(response.getSource());
		PrintUtils.showGetResponse(response);
		assertTrue(response.isExists());
	}
	/**
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 29, 2016
	 * 根据索引库，类型， ID 获取指定记录 
	 * 不使用多线程，会使用同一个节点
	 * 
	 */
	@Test
	public void testGetThreaded() throws Exception {
		GetResponse response = client.prepareGet("twitter", "tweet", "1")
		        .setOperationThreaded(false)
		        .get();
		PrintUtils.showGetResponse(response);
		assertTrue(response.isExists());
	}
}
