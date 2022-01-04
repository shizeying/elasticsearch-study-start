package com.run.start.tools;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CustomizeNullJsonComponent {
	public final static JsonSerializer<Object> NULL_ARRAY_SERIALIZER=new CustomizeNullJsonComponent.NullArrayJsonSerializer();
	
	public final static JsonSerializer<Object> NULL_STRING_SERIALIZER=new CustomizeNullJsonComponent.NullStringJsonSerializer();
	
	/**
	 * 處理陣列集合型別的null值
	 */
	public static class NullArrayJsonSerializer extends JsonSerializer<Object> {
		
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
			if (value == null) {
				gen.writeStartArray();
				gen.writeEndArray();
			} else {
				gen.writeObject(value);
			}
		}
	}
	/**
	 * 處理字串型別的null值
	 */
	public static class NullStringJsonSerializer extends JsonSerializer<Object> {
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
			if (value == null) {
				gen.writeString(StringUtils.EMPTY);
			} else {
				gen.writeObject(value);
			}
		}
	}
	/**
	 * 處理數值型別的null值
	 */
	public static class NullNumberJsonSerializer extends JsonSerializer<Object> {
		
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
			if (value == null) {
				gen.writeNumber(0);
			} else {
				gen.writeObject(value);
			}
		}
	}
	/**
	 * 處理boolean型別的null值
	 */
	public static class NullBooleanJsonSerializer extends JsonSerializer<Object> {
		
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
			if (value == null) {
				gen.writeBoolean(false);
			} else {
				gen.writeObject(value);
			}
		}
	}
	/**
	 * 處理實體物件型別的null值
	 */
	public static class NullObjectJsonSerializer extends JsonSerializer<Object> {
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
			if (value == null) {
				gen.writeStartObject();
				gen.writeEndObject();
			} else {
				gen.writeObject(value);
			}
		}
	}
	
}
