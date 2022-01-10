package com.run.start.tools;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Base64Exception extends Exception {
	static final long serialVersionUID = -7034897190745766930L;
	public Base64Exception(String message) {
		super(message);
	}
	
	public Base64Exception(Throwable cause) {
		super(cause);
	}
	

	
	public Base64Exception() {
	
	}
}
