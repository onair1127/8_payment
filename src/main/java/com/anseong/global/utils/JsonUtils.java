package com.anseong.global.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

	private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
     private static final JsonMapper mapper = JsonMapper.builder()
               .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
               .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
               .build();

     private JsonUtils() {}

     public static String toJson(Object object) {
          try {
               return mapper.writeValueAsString(object);
          } catch (JsonProcessingException e) {
               log.error("객체를 Json으로 write 하는데 실패.", e);
               return null;
          }
     }

     public static <T> T toObject(String json, Class<T> clazz) {
          try {
               return mapper.readValue(json, clazz);
          } catch (IOException e) {
               log.error("Json `" + json + "`을 객체 `" + clazz.getName() + "`로 읽어오는데 실패", e);
               return null;
          }
     }

     public static <T> T toObject(InputStream inputStream, Class<T> clazz) {
          try {
               return mapper.readValue(inputStream, clazz);
          } catch (IOException e) {
               log.error("InputStream을 객체 `" + clazz.getName() + "`로 읽어오는데 실패", e);
               return null;
          }
     }

     public static <T> T toObject(String json, JavaType javaType) {
          try {
               return mapper.readValue(json, javaType);
          } catch (IOException e) {
               log.error("Json `" + json + "`을 타입 `" + javaType.getTypeName() + "`로 읽어오는데 실패", e);
               return null;
          }
     }

     public static <T> T toObject(String json, Type type) {
          try {
               JavaType javaType = mapper.getTypeFactory().constructType(type);
               return mapper.readValue(json, javaType);
          } catch (IOException e) {
               log.error("Json `" + json + "`을 타입 `" + type.getTypeName() + "`로 읽어오는데 실패", e);
               return null;
          }
     }

     public static void write(Writer writer, Object value) throws IOException {
          mapper.writeValue(writer, value);
     }
}
