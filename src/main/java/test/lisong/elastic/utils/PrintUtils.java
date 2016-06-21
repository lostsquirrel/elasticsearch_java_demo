package test.lisong.elastic.utils;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

public class PrintUtils {

	public static void showIndexResponse(IndexResponse response) {
		// Index name
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		// isCreated() is true if the document is a new one, false if it has been updated
		boolean created = response.isCreated();
		
		String format = "索引名：%s， 类型：%s，文档ID:%s,文档版本：%s, 是否创建：%s";
		System.out.println(String.format(format , _index, _type, _id, _version, created));
	}
	
	public static void showGetResponse(GetResponse resp) {
		String format = "索引名：%s， 类型：%s，文档ID:%s,文档：%s";
		System.out.println(String.format(format, resp.getIndex(), resp.getType(),
				resp.getId(), resp.getSource()));
	}
	
	public static void showDelResponse(DeleteResponse resp) {
		String format = "索引名：%s， 类型：%s，文档ID:%s,是否找到文档：%s";
		System.out.println(String.format(format, resp.getIndex(), resp.getType(),
				resp.getId(), resp.isFound()));
	}
	
	public static void showUpdateResponse(UpdateResponse resp) {
		String format = "索引名：%s， 类型：%s，文档ID:%s,created：%s";
		System.out.println(String.format(format, resp.getIndex(), resp.getType(),
				resp.getId(), resp.isCreated()));
	}
}
