package com.xsy.base.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.log.IgnoreLog;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * JSON 工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER;
    private static final ObjectMapper LOG_OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = init(new ObjectMapper());
        LOG_OBJECT_MAPPER = initLogMapper(new ObjectMapper());
    }

    public static ObjectMapper init(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //日期格式转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(sdf);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        //Long类型转String类型
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    public static ObjectMapper initLogMapper(ObjectMapper objectMapper) {
        init(objectMapper);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean hasIgnoreMarker(AnnotatedMember m) {
                IgnoreLog ann = _findAnnotation(m, IgnoreLog.class);
                return ann != null;
            }
        });
        return objectMapper;
    }


    public static String toJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new GlobalException("json序列化失败", e);
        }
    }

    /**
     * 忽略{@link IgnoreLog}注解的字段
     *
     * @param object
     * @return
     */
    public static String toLogJsonString(Object object) {
        try {
            return LOG_OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new GlobalException("json序列化失败", e);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(text, clazz);
        } catch (Exception e) {
            throw new GlobalException("json反序列化失败", e);
        }
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(text, typeReference);
        } catch (Exception e) {
            throw new GlobalException("json反序列化失败", e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new GlobalException("json反序列化失败", e);
        }
    }
}
