package test.lisong.elastic.admin;

import static org.junit.Assert.*;

import java.util.Map;

import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;

import test.lisong.elastic.client.BaseTest;
import test.lisong.elastic.utils.ESUtils;

/**
 * @author 李嵩
 * 测试索引创建
 */
public class TestIndicesManagement extends BaseTest {

	private String indice = "docs";
	private String type = "index";

	/**
	 * 创建索引
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testCreateIndices() throws Exception {
		String indiceName = "docs_v0";
		String settings = "{ \"index.mapping.attachment.indexed_chars\" : -1, \"index.mapping.attachment.ignore_errors\": false, \"number_of_shards\": \"1\", \"number_of_replicas\": \"0\", \"analysis\": { \"analyzer\": { \"ik\": { \"type\": \"ik\", \"stem_exclusion\": [], \"stopwords\": [] } } } }";
		String mapping = "{ \"index\":{ \"properties\": { \"file\": { \"type\": \"attachment\", \"fields\": { \"content\": { \"type\": \"string\", \"term_vector\": \"with_positions_offsets\", \"analyzer\": \"ik\", \"store\": true }, \"author\"   : { \"store\" : \"yes\" }, \"title\"    : { \"store\" : \"yes\", \"analyzer\" : \"ik\"}, \"date\"     : { \"store\" : \"yes\" }, \"keywords\" : { \"store\" : \"yes\", \"analyzer\" : \"ik\" }, \"name\"    : { \"store\" : \"yes\" }, \"content_type\" : { \"store\" : \"yes\" } } }, \"cost_scores\": { \"type\": \"long\" }, \"document_type\": { \"type\": \"string\" }, \"download\": { \"type\": \"integer\" }, \"download_counts\": { \"type\": \"long\" }, \"file_size\": { \"type\": \"long\" }, \"file_type\": { \"type\": \"string\" }, \"filekey\": { \"type\": \"string\" }, \"industryno\": { \"type\": \"string\" }, \"keywords\": { \"type\": \"string\", \"fields\": { \"cn\": { \"type\": \"string\", \"analyzer\": \"ik\" }, \"en\": { \"type\": \"string\", \"analyzer\": \"english\" } } }, \"resource_scores\": { \"type\": \"float\" }, \"subject\": { \"type\": \"string\", \"fields\": { \"cn\": { \"type\": \"string\", \"analyzer\": \"ik\" }, \"en\": { \"type\": \"string\", \"analyzer\": \"english\" } } }, \"title\": { \"type\": \"string\", \"fields\": { \"cn\": { \"type\": \"string\", \"analyzer\": \"ik\" }, \"en\": { \"type\": \"string\", \"analyzer\": \"english\" } } }, \"update_date\": { \"type\": \"date\", \"format\": \"yyyy-MM-dd HH:mm:ss\" }, \"uploader\": { \"type\": \"string\" }, \"uploader_id\": { \"type\": \"string\" }, \"view_times\": { \"type\": \"long\" } } } }";
		
		boolean b = ESUtils.createIndice(indiceName, settings, mapping);
		assertTrue(b);
	}
	
	/**
	 * 添加别名
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testAddAlias() throws Exception {
		boolean b = ESUtils.addAlias("docs_v0", indice);
		assertTrue(b);
	}
	
	/**
	 * 获取索引配置
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testGetSettings() throws Exception {
		Map<String, String> settings = ESUtils.getSetting(indice);
		System.out.println(settings);
		assertFalse(settings.isEmpty());
	}
	
	/**
	 * 获取索引数据结构
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testGetMapping() throws Exception {
		Map<String, String> mapping = ESUtils.getMapping(indice, type);
		System.out.println(mapping);
		assertFalse(mapping.isEmpty());
	}
	
	/**
	 * 修改索引副本数量
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testChangeReplicas() throws Exception {
		UpdateSettingsResponse resp = client.admin().indices().prepareUpdateSettings(indice)   
        .setSettings(Settings.builder()                     
                .put("index.number_of_replicas", 1)
        )
        .get();
		
		assertTrue(resp.isAcknowledged());
	}
	
	/**
	 * 删除索引
	 * @author 李嵩
	 * @throws Exception
	 * @date Jun 30, 2016
	 */
	@Test
	public void testDeleteIndices() throws Exception {
		boolean b = ESUtils.removeIndice("docs_v0");
		assertTrue(b);
	}
}


/*
settings 索引库配置
{
    "settings": {
        "index.mapping.attachment.indexed_chars" : -1, // 上传附件全部内容
        "index.mapping.attachment.ignore_errors": false, // 如果附件处理出错则不索引此条记录
		"number_of_shards": "1", // 分片数， 每片一般可存储 integer.max -128条记录, 默认5 创建后不可修改
		"number_of_replicas": "0", // 副本数量，用于故障恢复，性能提升, 默认1 任何时候均可修改
		"analysis": { // 分词器
			"analyzer": {
				"ik": {
					"type": "ik",
					"stem_exclusion": [], // 不可分的词
					"stopwords": [] //停词，助词，如：了
				}
			}
		}

	},

    "aliases": {
		"docs": {} // 别名， 查询时使用，也方便索引更新替换
	}

}

mapping 数据结构
https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html
基本类型
long, integer, short, byte, double, float
binary
boolean
date
string
数据类型及JSON对应关系
Boolean: true or false ->boolean

Whole number: 123 ->long

Floating point: 123.45 ->double

String, valid date: 2014-09-15 ->date

String: foo bar ->string
{
    "index":{ // 类型名称
        "properties": {
            "file": {
                "type": "attachment", // 文档
                "fields": {
                    "content": {
                        "type": "string",
                        "term_vector": "with_positions_offsets",
                        "analyzer": "ik", // 对内容分词
                        "store": true
                    },

                    "author"   : { "store" : "yes" },
                    "title"    : { "store" : "yes", "analyzer" : "ik"},
                    "date"     : { "store" : "yes" },
                    "keywords" : { "store" : "yes", "analyzer" : "ik" },
                    "name"    : { "store" : "yes" },
                    "content_type" : { "store" : "yes" }
                }
            },
            "cost_scores": {
                "type": "long"
            },
            "document_type": {
                "type": "string"
            },
            "download": {
                "type": "integer"
            },
            "download_counts": {
                "type": "long"
            },
            "file_size": {
                "type": "long"
            },
            "file_type": {
                "type": "string"
            },
            "filekey": {
                "type": "string"
            },
            "industryno": {
                "type": "string"
            },
            "keywords": { // 同时进行中英文分词，应对 同时包含中英文时的情况，查询时要查询两个字段， keywords.cn, keywords.en
                "type": "string",
                "fields": {
                    "cn": {
                        "type": "string",
                        "analyzer": "ik" // 进行中文分词
                    },
                    "en": {
                        "type": "string",
                        "analyzer": "english" // 进行英文分词
                    }
                }
            },
            "resource_scores": {
                "type": "float"
            },
            "subject": {
                "type": "string",
                "fields": {
                    "cn": {
                        "type": "string",
                        "analyzer": "ik"
                    },
                    "en": {
                        "type": "string",
                        "analyzer": "english"
                    }
                }
            },
            "title": {
                "type": "string",
                "fields": {
                    "cn": {
                        "type": "string",
                        "analyzer": "ik"
                    },
                    "en": {
                        "type": "string",
                        "analyzer": "english"
                    }
                }
            },
            "update_date": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss"
            },
            "uploader": {
                "type": "string"
            },
            "uploader_id": {
                "type": "string"
            },
            "view_times": {
                "type": "long"
            }
        }
    }
}

 */
