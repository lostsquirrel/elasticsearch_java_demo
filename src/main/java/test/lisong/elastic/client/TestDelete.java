package test.lisong.elastic.client;

import static org.junit.Assert.*;

import org.elasticsearch.action.delete.DeleteResponse;
import org.junit.Test;

import test.lisong.elastic.utils.PrintUtils;

/**
 * @author 李嵩
 * 测试删除记录
 */
public class TestDelete extends BaseTest {

	/**
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 29, 2016
	 * 删除指定索引，类型，ID 的记录
	 */
	@Test
	public void testDelete() throws Exception {
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();
		
		PrintUtils.showDelResponse(response);
		assertTrue(response.isFound());
	}
	
}
