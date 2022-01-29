package top.ysqorz.forum.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 暂时不要用
 *
 * @Description: 自定义响应结构, 转换类
 */
public class JsonUtils {
    /**
     * 将对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        try {
            ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json结果集转化为对象（不包含泛型）
     *
     * @param json  json数据
     * @param clazz 对象中的object类型
     * @return
     */
    public static <T> T jsonToObj(String json, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode jsonToNode(String json) {
        try {
            ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T nodeToObj(JsonNode node, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
            return objectMapper.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode objToNode(Object obj) {
        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        return objectMapper.valueToTree(obj);
    }

    /**
     * 将json数据转换成pojo对象list
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
