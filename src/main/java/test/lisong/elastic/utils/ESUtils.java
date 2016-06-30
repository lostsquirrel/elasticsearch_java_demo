package test.lisong.elastic.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;

public class ESUtils {

	private static Client client;

	private static String DEFAULT_TYPE_NAME = "index";
	
	static {
		try {
			initClient("luyou_search", "192.168.1.13,192.168.1.14");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @author 李嵩
	 * @param clusterName 集群名称
	 * @param hosts 集群节点 host:port,host2:port2 如果不提供port则使用9300
	 * @return
	 * @throws UnknownHostException 
	 * @date Jun 16, 2016
	 */
	public static void initClient(String clusterName, String hosts) throws UnknownHostException {
		Settings settings = Settings.settingsBuilder()
		        .put("cluster.name", clusterName).build();
		TransportClient _client = TransportClient.builder().settings(settings).build();
		String[] _hosts = hosts.split(",");
		for (String host : _hosts) {
			if (hosts != null) {
				String[] _host = host.split(":");
				String h = _host[0];
				Integer p = null;
				if (_host.length == 1) {
					p = 9300;
				} else {
					p = Integer.parseInt(_host[1]);
				}
				_client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(h), p));
			}
		}
		client = _client;
	}
	
	public static Client getClient() {
		return client;
	}
	/**
	 * 计算上一版索引名称
	 * @author 李嵩
	 * @param indice
	 * @return
	 * @date Jun 22, 2016
	 */
	public static String getIndicePrev(String indice) {
		String[] xi = indice.split("_v");
		return String.format("%s_v%s", xi[0], Integer.parseInt(xi[1]) - 1);
	}
	/**
	 * 计算下一版索引名称
	 * @author 李嵩
	 * @param indice
	 * @return
	 * @date Jun 22, 2016
	 */
	public static String getIndiceNext(String indice) {
		String[] xi = indice.split("_v");
		return String.format("%s_v%s", xi[0], Integer.parseInt(xi[1]) + 1);
	}
	
	/**
	 * 计算索引别称
	 * @author 李嵩
	 * @param indice
	 * @return
	 * @date Jun 22, 2016
	 */
	public static String getIndiceAlias(String indice) {
		String[] xi = indice.split("_v");
		return xi[0];
	}
	
	/**
	 * 为索引添加别名
	 * @author 李嵩
	 * @param indice
	 * @param alias
	 * @return
	 * @date Jun 23, 2016
	 */
	public static boolean addAlias(String indice, String alias) {
		IndicesAliasesRequest request= new IndicesAliasesRequest()
				.addAlias(alias, indice);
		boolean res = true;
		try {
			IndicesAliasesResponse resp = client.admin().indices().aliases(request).get();
			res = resp.isAcknowledged();
		} catch (InterruptedException | ExecutionException e) {
			res = false;
		}
		
		return res;
	}
	
	/**
	 * 删除索引别名
	 * @author 李嵩
	 * @param indice
	 * @param alias
	 * @return
	 * @date Jun 23, 2016
	 */
	public static boolean removeAlias(String indice, String alias) {
		IndicesAliasesRequest request= new IndicesAliasesRequest()
				.removeAlias(indice, alias);
		boolean res = true;
		try {
			IndicesAliasesResponse resp = client.admin().indices().aliases(request).get();
			res = resp.isAcknowledged();
		} catch (InterruptedException | ExecutionException e) {
			res = false;
		}
		
		return res;
	}
	
	public static boolean removeIndice(String indice) {
		DeleteIndexResponse resp = client.admin().indices().prepareDelete(indice).get();
		return resp.isAcknowledged();
	}
	/**
	 * 获取索引配置信息
	 * @author 李嵩
	 * @param indice
	 * @return
	 * @date Jun 23, 2016
	 */
	public static Map<String, String> getSetting(String indice) {
		Client client = ESUtils.getClient();
		GetSettingsResponse response = client.admin().indices()
		        .prepareGetSettings(indice).get();
		ObjectObjectCursor<String, Settings> sett = response.getIndexToSettings().iterator().next();
		Settings settings = sett.value;
		List<String> removedKeys = Arrays.asList("index.creation_date", "index.uuid", "index.version.created");
	    Map<String, String> sm = settings.getAsMap();
	    Map<String, String> xsm = new HashMap<>();
	    for (Entry<String, String> key : sm.entrySet()) {
	    	if (!removedKeys.contains(key.getKey())) {
	    		xsm.put(key.getKey(), key.getValue());
	    	}
	    }
	    return xsm;
	}
	
	/**
	 * 获取索引结构信息
	 * @author 李嵩
	 * @param indice
	 * @param type
	 * @return
	 * @date Jun 23, 2016
	 */
	public static Map<String, String> getMapping(String indice, String type) {
		Map<String, String> res = new HashMap<>();
		Client client = ESUtils.getClient();
		GetMappingsRequest request = new GetMappingsRequest()
				.indices(indice).types(type);
		GetMappingsResponse m;
		try {
			m = client.admin().indices().getMappings(request).get();
			ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> m1 = m.getMappings();
			
			ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> m2 = m1.iterator().next();
			res.put("indice", m2.key);
			ImmutableOpenMap<String, MappingMetaData> m3 = m2.value;
			
			ObjectObjectCursor<String, MappingMetaData> m4 = m3.iterator().next();
			res.put("type", m4.key);
			
			MappingMetaData m5 = m4.value;
			String json = "{}";
			try {
				json = JsonUtil.toJson(m5.getSourceAsMap());
			} catch (Exception e) {
				e.printStackTrace();
			}
			res.put("mapping", json);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * 获取索引结构信息
	 * @author 李嵩
	 * @param indice
	 * @param type
	 * @return
	 * @date Jun 23, 2016
	 */
	public static Map<String, String> getMapping(String indice) {
		Map<String, String> res = new HashMap<>();
		Client client = ESUtils.getClient();
		GetMappingsRequest request = new GetMappingsRequest()
				.indices(indice).types(DEFAULT_TYPE_NAME);
		GetMappingsResponse m;
		try {
			m = client.admin().indices().getMappings(request).get();
			ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> m1 = m.getMappings();
			Iterator<ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>>> it1 = m1.iterator();
			ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> m2 = it1.next();
			while(it1.hasNext()) {
				m2 = it1.next();
			}
			res.put("indice", m2.key);
			ImmutableOpenMap<String, MappingMetaData> m3 = m2.value;
			
			ObjectObjectCursor<String, MappingMetaData> m4 = m3.iterator().next();
			res.put("type", m4.key);
			
			MappingMetaData m5 = m4.value;
			String json = "{}";
			try {
				json = JsonUtil.toJson(m5.getSourceAsMap());
			} catch (Exception e) {
				e.printStackTrace();
			}
			res.put("mapping", json);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static Map<String, Object> createIndexBulk(List<Map<String, Object>> paraList, String indice) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Map<String, Object> parameter: paraList) {
			String id = parameter.remove("id").toString();
			String json;
			try {
				json = JsonUtil.toJson(parameter);
				bulkRequest.add(client.prepareIndex(indice, DEFAULT_TYPE_NAME, id)
						.setSource(json)
						);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		BulkResponse bulkResponse = bulkRequest.get();
		BulkItemResponse[] items = bulkResponse.getItems();
		
		Map<String, Object> res = new HashMap<>();
		for (BulkItemResponse item : items) {
			String failureMessage = item.getFailureMessage();
			if (failureMessage != null){
				res.put(item.getId(), failureMessage);
			}
		}
		return res;
	}
	
	public static Map<String, Object> createIndex(Map<String, Object> parameter, String indice){
		Map<String, Object> res = new HashMap<String, Object>();
		Client client = ESUtils.getClient();
		String id = parameter.remove("id").toString();
		try {
			String json = JsonUtil.toJson(parameter);
			
			client.prepareIndex(indice, DEFAULT_TYPE_NAME)
					.setSource(json)
					.setId(id)
					.get();
			
		} catch (Exception e) {
			res.put("id", e.getMessage());
		}
		
		return res;
	}
	
	/**
	 * 使用有索引库配置创建新索引库
	 * @author 李嵩
	 * @param indiceName
	 * @return
	 * @date Jun 23, 2016
	 */
	public static String createIndice(String indiceName) {
		Map<String, String> mapping = getMapping(indiceName);
		Map<String, String> setting = getSetting(indiceName);
		String indiceCurrent = mapping.get("indice");
		String mappingJson = mapping.get("mapping");
		
		String indiceNext = ESUtils.getIndiceNext(indiceCurrent);
		try {
			client.admin().indices().prepareCreate(indiceNext)
			.setSettings(setting)
			.addMapping("index", mappingJson).get();
		} catch (Exception e) {}
		return indiceCurrent;
	}
	
	/**
	 * 使用配置创建索引
	 * @author 李嵩
	 * @param indiceName
	 * @param settings
	 * @param mapping
	 * @return
	 * @date Jun 30, 2016
	 */
	public static boolean createIndice(String indiceName, String settings, String mapping){
		CreateIndexResponse resp = client.admin().indices().prepareCreate(indiceName)
		.setSettings(settings)
		.addMapping("index", mapping).get();
		
		return resp.isAcknowledged();
	}
	/**
	 * 关闭链接
	 * @author 李嵩
	 * @date Jun 29, 2016
	 */
	public static void close() {
		client.close();
	}
}
