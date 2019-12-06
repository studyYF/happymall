package com.mmall.util;

import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by YangFan.
 * @date 2019/11/27
 * 功能: json序列化和反序列化工具
 */
@Slf4j
public class JsonUtil {


    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式统一格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略json字符串中存在，但是在java对象中不存在的对应属性的情况，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 将对象转为string
     *
     * @param obj 对西那个
     * @param <T> 泛型
     * @return string
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse json error", e);
            return null;
        }
    }

    /**
     * 将对象转为string，格式化好的
     *
     * @param obj 对西那个
     * @param <T> 泛型
     * @return string
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj :
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse json error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> tClass) {
        if (StringUtils.isEmpty(str) || tClass == null) {
            return null;
        }
        try {
            return tClass.equals(String.class) ? (T) str : objectMapper.readValue(str, tClass);
        } catch (Exception e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass,
                                   Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass
                , elementClasses);

        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }


    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str :
                    objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }


    public static void main(String[] args) {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("crossyf@mmall.com");

        String user1Json = JsonUtil.obj2String(user1);

        String user1JsonPretty = JsonUtil.obj2StringPretty(user1);

        log.info(user1Json);
        log.info("PettyJson : {}", user1JsonPretty);

        User user = JsonUtil.string2Obj(user1Json, User.class);
        System.out.printf("end");

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("crossyf2@mmall.com");


        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        String userLisString = JsonUtil.obj2StringPretty(userList);
        log.info("list++++++{}", userLisString);


        List<User> userList1 = JsonUtil.string2Obj(userLisString, new TypeReference<List<User>>() {
        });
        System.out.println(userList1);

        List<User> userList2 = JsonUtil.string2Obj(userLisString, List.class, User.class);
        System.out.println(",,,,");

    }


}
