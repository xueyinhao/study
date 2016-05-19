package org.hao.basic.model;

public class BasicException extends RuntimeException {

	private static final long serialVersionUID = 538922474277376456L;
	private String backUrl;
	private String text;

	public String getBackUrl() {
		return this.backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public BasicException() {
		
	}

	public BasicException(BasicException e) {
		super(e.getMessage());
		this.backUrl = e.getBackUrl();
	}

	public BasicException(String message) {
		super(message);
	}

	public BasicException(String message, String backUrl) {
		super(message);
		this.backUrl = backUrl;
	}

	public BasicException(String message, String backUrl, String text) {
		super(message);
		this.backUrl = backUrl;
		this.text = text;
	}
}