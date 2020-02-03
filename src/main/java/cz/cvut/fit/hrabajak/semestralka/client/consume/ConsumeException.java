package cz.cvut.fit.hrabajak.semestralka.client.consume;

import java.io.IOException;

public class ConsumeException extends RuntimeException {

	String baseMessage;

	public ConsumeException(String message) {
		super(message);
		this.baseMessage = message;
	}

	@Override
	public String getMessage() {
		return this.baseMessage;
	}
}
