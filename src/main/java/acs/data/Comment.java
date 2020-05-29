package acs.data;

import javax.persistence.Embeddable;

@Embeddable
public class Comment {
	private String header;
	private String content;
	
	public Comment() {
		super();
	}
	public Comment(String header, String content) {
		setHeader(header);
		setContent(content);
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
