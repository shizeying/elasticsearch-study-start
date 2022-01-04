package com.run.start.tools;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

import java.util.Objects;

public class CustomSerializerProvider extends DefaultSerializerProvider {
	public CustomSerializerProvider() {
		super();
	}
	
	public CustomSerializerProvider(CustomSerializerProvider provider, SerializationConfig config,
	                                SerializerFactory jsf) {
		super(provider, config, jsf);
	}
	
	@Override
	public CustomSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
		return new CustomSerializerProvider(this, config, jsf);
	}
	


	@Override
	public JsonSerializer<Object> findValueSerializer(final Class<?> valueType, final BeanProperty property) throws JsonMappingException {
		
		
		if (Objects.nonNull(property) && property.getType().getRawClass().equals(String.class)) {
			return Serializers.NOT_NULL_STRING_SERIALIZER_INSTANCE;
		} else
			return super.findValueSerializer(valueType, property);
	}
	
	
	@Override
	public JsonSerializer<Object> findValueSerializer(final JavaType valueType, final BeanProperty property) throws JsonMappingException {
		
		
		if (Objects.nonNull(property) && property.getType().getRawClass().equals(String.class)) {
			
			return Serializers.NOT_NULL_STRING_SERIALIZER_INSTANCE;
		} else {
			return super.findValueSerializer(valueType, property);
			
		}
		
	}
	
	@Override
	public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
		if (property.getType().getRawClass().equals(String.class))
			return Serializers.EMPTY_STRING_SERIALIZER_INSTANCE;
		else
			return super.findNullValueSerializer(property);
	}
	
	
	
	

	

	
	@Override
	public JsonSerializer<Object> findTypedValueSerializer(final Class<?> valueType, final boolean cache, final BeanProperty property) throws JsonMappingException {
	
		
		return super.findTypedValueSerializer(valueType, cache, property);
	}
	
	@Override
	public JsonSerializer<Object> findTypedValueSerializer(final JavaType valueType, final boolean cache, final BeanProperty property) throws JsonMappingException {
		System.out.println("aaa5");
		return super.findTypedValueSerializer(valueType, cache, property);
	}
	
	@Override
	public JsonSerializer<Object> findKeySerializer(final JavaType keyType, final BeanProperty property) throws JsonMappingException {
		System.out.println("aaa4");
		return super.findKeySerializer(keyType, property);
	}
	
	
	@Override
	public JsonSerializer<?> handlePrimaryContextualization(final JsonSerializer<?> ser, final BeanProperty property) throws JsonMappingException {
		System.err.println("aaa3");
		if (Objects.nonNull(property)){
			System.err.println(property.getName());
		}
		
		return super.handlePrimaryContextualization(ser, property);
	}
	

	

}
