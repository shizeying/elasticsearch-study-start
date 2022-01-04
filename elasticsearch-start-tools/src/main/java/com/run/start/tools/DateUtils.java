package com.run.start.tools;

import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateUtils {
	public final static String format = "yyyy-MM-dd";
	
	public static String dealDate(String oldDateStr) {
		if (StringUtils.isAllBlank(oldDateStr)) return null;
		
		Date date = Try.of(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH))
				
				.mapTry(df -> df.parse(oldDateStr))
				.getOrElse(() -> Try.of(() -> new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)
								.parse(oldDateStr.toString()))
						.getOrElse(
								Try.of(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
												.parse(oldDateStr.toString()))
										.getOrElse(
												Try.of(() -> new SimpleDateFormat("MM/dd/yy h:mma", Locale.ENGLISH)
																.parse(oldDateStr.toString()))
														.getOrElse(
																Try.of(() -> new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH)
																				.parse(oldDateStr.toString()))
																		.getOrElse(
																				Try.of(() -> new SimpleDateFormat("MM/dd/yy-h:mma", Locale.ENGLISH)
																								.parse(oldDateStr.toString()))
																						.getOrElse(
																								Try.of(() -> new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
																												.parse(oldDateStr.toString()))
																										.getOrElse(
																												
																												Try.of(() -> new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
																																.parse(oldDateStr.toString()))
																														.getOrElse(
																																Try.of(() -> new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
																																				.parse(oldDateStr.toString()))
																																		.getOrElse(
																																				Try.of(() -> new Date(oldDateStr.toString()))
																																						.getOrNull()
																																		)
																														)
																										)
																						)
																		)
														)
										)
						
						
						)
				
				
				);
		if (Objects.isNull(date)) return null;
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
		
		
	}
	
	public static boolean isDate(Object obj) {
		if (Objects.isNull(obj)) return false;
		if (!(obj instanceof String)) return false;
		if (StringUtils.isAllBlank(String.valueOf(obj))) return false;
		return Objects.nonNull(dealDate(String.valueOf(obj)));
		
	}
	
	public static boolean checkTime(Long obj) {
		if (Objects.isNull(obj)) return false;
		return String.valueOf(obj).length() >= 10;
	}
	
	public static String timeConvertDate_yyyy_mm_dd(Long obj) {
		return Try.of(() -> new Date(obj))
				.mapTry(date -> new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date))
				.getOrNull();
	}
	

}
