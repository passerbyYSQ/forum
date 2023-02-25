package top.ysqorz.forum.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * ObjectMapper(被我们定制过)的工具类
 */
@Component
public class JsonUtils {
    private static ObjectMapper objectMapper;

    // 静态变量可通过构造方法注入
    @Autowired
    public JsonUtils(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
    }

    /**
     * 将对象转换成json字符串
     */
    public static String objToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String objToPrettyJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json字符串转化为指定对象(不包含泛型)
     */
    public static <T> T jsonToObj(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过序列化实现对象的深度克隆或转化
     */
    public static <T> T convertBySerialization(Object obj, Class<T> clazz) {
        if (obj.getClass().isAssignableFrom(clazz)) {
            return clazz.cast(obj);
        }
        return jsonToObj(objToJson(obj), clazz);
    }

    /**
     * 尽量不要使用JsonNode对象，Hutool的JSONObject对象更强大
     *
     * @see cn.hutool.json.JSONObject
     */
    public static JsonNode jsonToNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T nodeToObj(JsonNode node, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode objToNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }

    /**
     * 将json数据转换成pojo对象list
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
