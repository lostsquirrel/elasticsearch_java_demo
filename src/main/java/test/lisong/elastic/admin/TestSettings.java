package test.lisong.elastic.admin;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;

import test.lisong.elastic.utils.ESUtils;
import test.lisong.elastic.utils.JsonUtil;

public class TestSettings {

	@Test
	public void testGetSettings() throws Exception {
		
		Client client = ESUtils.getClient();
		
		GetSettingsResponse response = client.admin().indices()
		        .prepareGetSettings("courses").get();                           
		for (ObjectObjectCursor<String, Settings> cursor : response.getIndexToSettings()) { 
		    String index = cursor.key; 
		    System.out.println(index);
		    Settings settings = cursor.value;                                               
		    Integer shards = settings.getAsInt("index.number_of_shards", null);
		    System.out.println(shards);
		    Integer replicas = settings.getAsInt("index.number_of_replicas", null);         
		    System.out.println(replicas);
		    List<String> removedKeys = Arrays.asList("index.creation_date", "index.uuid", "index.version.created");
		    Map<String, String> sm = settings.getAsMap();
		    Map<String, String> xsm = new HashMap<>();
		    for (Entry<String, String> key : sm.entrySet()) {
		    	if (!removedKeys.contains(key.getKey())) {
		    		xsm.put(key.getKey(), key.getValue());
		    	}
		    }
		    System.out.print(JsonUtil.toJson(xsm));
		}
	
	}
	
	
	@Test
	public void testAlias() throws Exception {
		
		String indiceCurrent = "courses_v2";
		IndicesAliasesResponse resp = changeAlias(indiceCurrent);
		
		System.out.println(resp);
	}


	private IndicesAliasesResponse changeAlias(String indiceCurrent)
			throws InterruptedException, ExecutionException {
		Client client = ESUtils.getClient();
		
		String indiceAlias = ESUtils.getIndiceAlias(indiceCurrent);
		String indiceNext = ESUtils.getIndiceNext(indiceCurrent);
		IndicesAliasesRequest request= new IndicesAliasesRequest()
				.addAlias(indiceAlias, indiceNext)
				.removeAlias(indiceCurrent, indiceAlias);
		IndicesAliasesResponse resp = client.admin().indices().aliases(request).get();
		return resp;
	}
	
	
	@Test
	public void testCreateIndex() throws Exception {
		String indiceName = "courses";
		String indiceCurrent = createIndice(indiceName);
		
		changeAlias(indiceCurrent);
	}


	private String createIndice(String indiceName) {
		Map<String, String> mapping = getMapping(indiceName, "index");
		Map<String, String> setting = getSetting(indiceName);
		String indiceCurrent = mapping.get("indice");
		String mappingJson = mapping.get("mapping");
		Client client = ESUtils.getClient();
		client.admin().indices().prepareCreate(ESUtils.getIndiceNext(indiceCurrent))
			.setSettings(setting)
			.addMapping("index", mappingJson).get();
		return indiceCurrent;
	}
	
	@Test
	public void testGetMapping() throws Exception {
		System.out.println(getMapping("courses", "index"));
	}
	
	public Map<String, String> getSetting(String indice) {
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
	
	public Map<String, String> getMapping(String indice, String type) {
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
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			res.put("mapping", json);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return res;
	}
}
