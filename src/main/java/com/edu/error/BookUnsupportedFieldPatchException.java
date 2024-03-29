package com.edu.error;

import java.util.Set;

public class BookUnsupportedFieldPatchException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookUnsupportedFieldPatchException(Set<String> keys) {
		super("Field " + keys.toString() + " update is not allowed");
	}
}
