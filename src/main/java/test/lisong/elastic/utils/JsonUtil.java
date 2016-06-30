package test.lisong.elastic.utils;

import java.io.IOException;
import java.util.List;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * 
 * @author Lipx
 * @Description 
 * @revise
 * @time 2015年4月7日 下午4:48:03
 * @version 1.0
 * @copyright Copyright @2014,  Co., Ltd. All right.
 */
public class JsonUtil {


    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) throws Exception {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new Exception("json转换异常", e);
        } catch (Exception e) {
            throw new Exception("json转换异常", e);
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) throws Exception {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new Exception("json转换异常", e);
        } catch (Exception e) {
            throw new Exception("json转换异常", e);
        }
    }

    @SuppressWarnings("deprecation")
	public static <T> T toObject(String json, Class<T> arg1, Class<?> arg2) throws Exception {
        TypeFactory t = TypeFactory.defaultInstance();
        T obj = null;
        try {
            obj = mapper.readValue(json,
 t.constructParametricType(arg1, arg2));
        } catch (IOException e) {
            throw new Exception("json转换异常", e);
        } catch (Exception e) {
            throw new Exception("json转换异常", e);
        }
        return obj;
    }

    public static <T> List<T> toObjectList(String json, Class<T> clazz) throws Exception {
        try {
            return mapper.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            throw new Exception("json转换异常", e);
        } catch (Exception e) {
            throw new Exception("json转换异常", e);
        }
    }

    public static String getProperty(String json, String nodeName) throws Exception {
        try {
            JsonNode node = mapper.readTree(json);
            return node.get(nodeName).textValue();
        } catch (IOException e) {
            throw new Exception("json转换异常", e);
        } catch (Exception e) {
            throw new Exception("json转换异常", e);
        }
    }
}
