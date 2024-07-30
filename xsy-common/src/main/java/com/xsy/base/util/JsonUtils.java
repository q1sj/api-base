package com.xsy.base.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.log.IgnoreLog;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * JSON 工具类
 *
 * @author Q1sj
 */
public class JsonUtils {
    public static int maxLogStringLength = 10 * 1024;
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
        SimpleModule module = new SimpleModule();
        module.addSerializer(Map.class, new MapStringTruncateSerializer());
        module.addSerializer(String.class, new StringTruncateSerializer());
        objectMapper.registerModule(module);
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


    public static class MapStringTruncateSerializer extends JsonSerializer<Map> {

        @Override
        public void serialize(Map value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            Map<?, ?> map = value;
            gen.writeStartObject();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = Objects.toString(entry.getKey());
                Object entryValue = entry.getValue();
                if (entryValue instanceof String) {
                    String strValue = (String) entryValue;
                    if (strValue.length() > maxLogStringLength) {
                        strValue = strValue.substring(0, maxLogStringLength) + "...长度:" + strValue.length();
                    }
                    gen.writeStringField(key, strValue);
                } else {
                    serializers.defaultSerializeField(key, entryValue, gen);
                }
            }
            gen.writeEndObject();
        }
    }

    public static class StringTruncateSerializer extends JsonSerializer<String> {

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value.length() > maxLogStringLength) {
                value = value.substring(0, maxLogStringLength) + "...长度:" + value.length();
            }
            gen.writeString(value);
        }
    }

}
