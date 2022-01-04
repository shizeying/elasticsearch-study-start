package com.run.start.tools;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j

public class Serializers extends JsonSerializer<Object> {
	public static final JsonSerializer<Object> EMPTY_STRING_SERIALIZER_INSTANCE = new EmptyStringSerializer();
	public static final JsonSerializer<Object> NOT_NULL_STRING_SERIALIZER_INSTANCE = new NotNullStringSerializer();
	public Serializers() {}
	@Override
	public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeString("");
	}
	private static class EmptyStringSerializer extends JsonSerializer<Object> {
		public EmptyStringSerializer() {}
		
	
		
		@Override
		public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException, JsonProcessingException {
			log.info("进入value为空的序列化.....");
			System.out.println("进入value为空的序列化");
			jsonGenerator.writeObject(o);
		}
	}
	private static class NotNullStringSerializer extends JsonSerializer<Object> {
		public NotNullStringSerializer() {}
		@Override
		public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException, JsonProcessingException {
			 if (Objects.nonNull(o)){
				 String temp=(String) o;
				 final JsonNode newJsonNot = Try.of(() -> JacksonUtil.readJson(temp))
						 .getOrElse(() ->
								            Try.of(() -> JSON.parse(temp))
										            .mapTry(JSON::toJSONString)
										            .mapTry(JacksonUtil::readJson)
										            .getOrNull()
						 );
				 
				 if (Objects.nonNull(newJsonNot)){
					 final String s = JacksonUtil.bean2Json(newJsonNot);
					 System.out.println(s);
					 jsonGenerator.writeObject(JacksonUtil.readJson(s));
					
				 }else {
					 if (DateUtils.isDate(o)){
						 final String newDate = DateUtils.dealDate((String) o);
						 jsonGenerator.writeObject(newDate);
					 } else {
						 jsonGenerator.writeObject(o);
					 }
					 
				 }
				
				
			 }else {
				 jsonGenerator.writeObject(o);
			 }
			
			
		}
	}
}
