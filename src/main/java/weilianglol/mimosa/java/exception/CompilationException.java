package weilianglol.mimosa.java.exception;

import java.io.ByteArrayOutputStream;

public class CompilationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CompilationException(ByteArrayOutputStream baos) {
		super(baos.toString());
	}

}
