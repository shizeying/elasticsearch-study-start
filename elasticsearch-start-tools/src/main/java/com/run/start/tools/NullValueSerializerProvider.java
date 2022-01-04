package com.run.start.tools;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

import java.util.Collection;
import java.util.Objects;

/**
 * @Author：LJ
 * @Description： <div>
 * 該類的作用是使用SpringBoot自動配置的Jackson ObjectMapper支援將null的陣列轉為[]、null字串轉為""
 * </div>
 * <p>
 * 1.發現{@link SerializerProvider#findNullValueSerializer(BeanProperty)}用於序列化屬性值為null；
 * 而該介面的預設實現是通過{@link SerializerProvider#getDefaultNullValueSerializer()}獲取的{@link SerializerProvider#_nullValueSerializer};
 * 預設初始為{@link com.fasterxml.jackson.databind.ser.std.NullSerializer#instance}
 * <p>
 * 2.因為{@link SerializerProvider}為抽象類；
 * -->{@link DefaultSerializerProvider}也為抽象類，並且所有自定義的{@link SerializerProvider}都必須繼承此類；因為{@link ObjectMapper}需要這個型別
 * -->預設實現類{@link DefaultSerializerProvider.Impl},該類在{@link ObjectMapper#ObjectMapper(JsonFactory, DefaultSerializerProvider, DefaultDeserializationContext)}的
 * 建構函式中有如下一行程式碼：
 * <code>
 * _serializerProvider = (sp == null) ? new DefaultSerializerProvider.Impl() : sp;
 * </code>
 * 我們可以看到，在沒有指定DefaultSerializerProvider的時候，預設例項化DefaultSerializerProvider.Impl()
 * <br>
 * 3.下面我們就來理一下SpringBoot是怎麼例項化ObjectMapper
 * 首先定位{@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration}配置類
 * -->在{@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration#jacksonObjectMapperBuilder(List)}方法上發現了
 * <code>
 * @ConditionalOnMissingBean(Jackson2ObjectMapperBuilder.class) </code>
 * 也就是在沒有Jackson2ObjectMapperBuilder時會執行jacksonObjectMapperBuilder(List)方法；
 * 而JacksonAutoConfiguration配置類中的其他內容都是
 * <code>
 * @ConditionalOnClass({ ObjectMapper.class, Jackson2ObjectMapperBuilder.class })
 * </code>
 * 需要依託Jackson2ObjectMapperBuilder和ObjectMapper
 * 4.通過第3步我們定位到Jackson2ObjectMapperBuilder實在JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration#jacksonObjectMapperBuilder(List)中例項的，那麼ObjectMapper呢？
 * -->在{@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration#jacksonObjectMapper(Jackson2ObjectMapperBuilder)}中例項化的
 * 該方法被
 * <code>
 * @ConditionalOnMissingBean(ObjectMapper.class) </code>
 * 註解修飾，並且傳遞Jackson2ObjectMapperBuilder引數，通過{@link Jackson2ObjectMapperBuilder#build()}構造ObjectMapper
 * -->進而調整用{@link ObjectMapper#ObjectMapper()}
 * -->{@link ObjectMapper#ObjectMapper(JsonFactory, DefaultSerializerProvider, DefaultDeserializationContext)}
 * 在此處就對接到了第2步中<pre>_serializerProvider = (sp == null) ? new DefaultSerializerProvider.Impl() : sp;</pre>
 * <p>
 * 總結：
 * 所以本類通過繼承{@link DefaultSerializerProvider}實現自定義的null處理轉換邏輯
 * </p>
 * @Date: 2020/11/25
 * @Modified By:
 */
public class NullValueSerializerProvider extends DefaultSerializerProvider {
	
	public NullValueSerializerProvider() {
		super();
	}
	
	protected NullValueSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
		super(src, config, f);
	}
	
	@Override
	public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
		return new NullValueSerializerProvider(this, config, jsf);
	}
	
	@Override
	public JsonSerializer<Object> findValueSerializer(final Class<?> valueType, final BeanProperty property) throws JsonMappingException {
		if (Objects.nonNull(property) && property.getType().getRawClass().equals(String.class)) {
			return Serializers.NOT_NULL_STRING_SERIALIZER_INSTANCE;
		} else
			return super.findValueSerializer(valueType, property);
		// return super.findValueSerializer(valueType, property);
	}
	
	@Override
	public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
		if (isStringType(property)) {
			return Serializers.NOT_NULL_STRING_SERIALIZER_INSTANCE;
		} else if (isArrayType(property)) {
			return CustomizeNullJsonComponent.NULL_ARRAY_SERIALIZER;
		} else {
			return super.findNullValueSerializer(property);
		}
	}
	
	/**
	 * 是否是陣列
	 */
	private boolean isArrayType(BeanProperty property) {
		Class<?> clazz = property.getType().getRawClass();
		return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
	}
	
	/**
	 * 是否是String
	 */
	private boolean isStringType(BeanProperty property) {
		Class<?> clazz = property.getType().getRawClass();
		return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
	}
}
