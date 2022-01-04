package com.run.start.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Objects;


public class MyErrorException extends Exception {
	private Class clazz;
	

	
	public MyErrorException(final Throwable cause) {
		// super(cause);
	}
	
	@Override
	public void printStackTrace() {
		// super.printStackTrace();
	}
	
	@Override
	public void printStackTrace(final PrintStream s) {
		//super.printStackTrace(s);
	}
	
	
	protected MyErrorException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, true,true);
		System.out.println(cause.getMessage());
		
		
	}
	
	public MyErrorException(final Throwable err, Object obj) {
		
		final Logger logger = LoggerFactory.getLogger(clazz);
		for (final StackTraceElement element : err.getStackTrace()) {
			final String clazzName = clazz.getName();
			if (StringUtils.equalsAnyIgnoreCase(clazzName, element.getClassName())) {
				
				String errorMessage = Objects.isNull(err.getCause()) ? err.getMessage() : err.getCause().getMessage();
				logger.error("className:[{}];FileName:[{}];lineNumber:[{}];methodName:[{}];error message:[{}];data:{}", element.getClassName(), element.getFileName(), element.getLineNumber(), element.getMethodName(), errorMessage, obj);
				
				break;
			}
		}
	}
	
	
	
	
}
