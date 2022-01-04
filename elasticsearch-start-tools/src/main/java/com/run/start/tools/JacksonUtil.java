package com.run.start.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.function.Function;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN;
import static com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS;
import static com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS;
import static com.fasterxml.jackson.core.json.JsonWriteFeature.QUOTE_FIELD_NAMES;

/**
 * jackson 工具类
 *
 * @author shizeying
 * @date 2021/06/12
 */
@Slf4j
public class JacksonUtil {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 设置转换日期类型的时间科室,如果不设置默认打印Timestamp毫秒数.
	 *
	 * @param pattern 时间格式化字符串
	 */
	public void setDateFormat(String pattern) {
		if (StringUtils.isNotBlank(pattern)) {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			mapper.getSerializationConfig().with(dateFormat);
			mapper.getDeserializationConfig().with(dateFormat);
		}
	}
	
	/**
	 * 设置是否使用Enum的toString函数来读取Enum,为false时使用Enum的name()函数类读取Enum, 默认为false.
	 */
	public void enableEnumUseToString() {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}
	
	/**
	 * 支持使用Jaxb的Annotation,使得POJO上的annotation不用与Jackson藕合.
	 * 默认会先查找jaxb的annotation,如果找不到再找jackson的.
	 */
	public void enableJaxbAnnotation() {
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		mapper.registerModule(module);
	}
	
	public static void mapperConfig() {
		//这个特性，决定了解析器是否将自动关闭那些不属于parser自己的输入源。
		// 如果禁止，则调用应用不得不分别去关闭那些被用来创建parser的基础输入流InputStream和reader；
		//默认是true
		mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
		//是否允许解析使用Java/C++ 样式的注释（包括'/'+'*' 和'//' 变量）
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		
		//设置为true时，属性名称不带双引号
		// mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
		mapper.writer().with(QUOTE_FIELD_NAMES);
		//反序列化是是否允许属性名称不带双引号
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		
		//是否允许单引号来包住属性名称和字符串值
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		
		//是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
		// mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		mapper.reader().with(ALLOW_UNESCAPED_CONTROL_CHARS);
		
		//是否允许JSON整数以多个0开始  ALLOW_LEADING_ZEROS_FOR_NUMBERS
		// mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
		mapper.reader().with(ALLOW_LEADING_ZEROS_FOR_NUMBERS);
		
		//null的属性不序列化
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		
		//按字母顺序排序属性,默认false
		mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		
		//是否以类名作为根元素，可以通过@JsonRootName来自定义根元素名称,默认false
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		
		//是否缩放排列输出,默认false
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		
		//序列化Date日期时以timestamps输出，默认true
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		
		//序列化枚举是否以toString()来输出，默认false，即默认以name()来输出
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		
		//序列化枚举是否以ordinal()来输出，默认false
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
		
		//序列化单元素数组时不以数组来输出，默认false
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		
		//序列化Map时对key进行排序操作，默认false
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		
		//序列化char[]时以json数组输出，默认false
		mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
		//序列化BigDecimal时是输出原始数字还是科学计数，默认false，即以toPlainString()科学计数方式来输出
		// mapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		mapper.writer().with(WRITE_BIGDECIMAL_AS_PLAIN);
		
		
	}
	
	public static String bean2Json(Object obj) {
		//允许出现单引号
		// mapperConfig();
		
		//允许出现特殊字符和转义符                     ALLOW_UNESCAPED_CONTROL_CHARS
		mapper.setSerializerProvider(new CustomSerializerProvider())
		
		
		;
		
		return Try.of(() -> mapper.writeValueAsString(obj)).onFailure(error -> log.error("JacksonUtil:[{}]", error.getMessage())).get();
	}
	
	public static String bean2JsonNotNUll(Object obj) {
		return Try.of(() -> mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(obj)).onFailure(error -> log.error("JacksonUtil:[{}]", error.getMessage())).get();
	}
	
	public static Function<Object, String> bean2JsonFun = obj -> Try.of(() -> mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(obj)).onFailure(error -> log.error("JacksonUtil:[{}]", error.getMessage())).get();
	
	public static <T> T json2BeanByType(byte[] data, Class<T> clazz) {
		
		return Try.of(() -> mapper.readValue(data, clazz)).onFailure(error -> log.error("JacksonUtil:[{}]", error.getMessage())).get();
	}
	
	public static <T> T json2BeanByTypeReference(String jsonStr, TypeReference<T> toValueTypeRef) {
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return Try.of(() -> mapper.readValue(jsonStr, toValueTypeRef)).onFailure(error -> log.error("JacksonUtil:[{}]", error.getMessage())).get();
	}
	
	public static <T> T json2LongByTypeReference(String jsonStr, TypeReference<T> toValueTypeRef) {
		return Try.of(() -> mapper.readValue(jsonStr, toValueTypeRef)).onFailure(error -> log.error("JacksonUtil:[{}]", error.getMessage())).get();
	}
	
	public static <T> Map convertValue(T entity) {
		return mapper.convertValue(entity, Map.class);
	}
	
	public static JsonNode readJson(String content) {
		return Try.of(() -> mapper.readTree(content)).onFailure(err -> new MyErrorException(err, JacksonUtil.class)).get();
	}
	
	public static void main(String[] args) {
		String json = " {\n" + "     \"elasticsearchIndex\":\"2021-11-11 11:11:11\",\n" + "     \"inputJson\":{\n" + "         \"id\":\"id\",\n" + "          \"id2\":\"id\"\n" + "     }}";
		final OBj oBjBuilder = OBj.builder().key("2021-11-11 11:11:11").tag(json).build();
		final String json1 = JacksonUtil.bean2Json(oBjBuilder);
		final JsonNode jsonNode = JacksonUtil.readJson(json1);
		System.err.println(JacksonUtil.bean2Json(jsonNode));
		
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OBj {
		private String tag;
		private String key;
	}
	
	
}
