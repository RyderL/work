package com.zterc.uos.fastflow.exception;

public class WMUnsupportedOperationException extends FastflowException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WMUnsupportedOperationException(String message) {
		super(message);
	}

	public WMUnsupportedOperationException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public WMUnsupportedOperationException(Throwable throwable) {
		super(throwable);
	}
}
