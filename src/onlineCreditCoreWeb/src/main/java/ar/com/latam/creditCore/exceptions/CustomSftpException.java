package ar.com.latam.creditCore.exceptions;

public class CustomSftpException extends Exception {
	private String message;

	public CustomSftpException(String message) {
		this.message = message;
	}

}
