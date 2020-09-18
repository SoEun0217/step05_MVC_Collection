package kosta.mvc.model.dto;

public class Board {
	private int no;
	private String subject;
	private String writer;
	private String content;
	private String date;
	public Board(int no, String subject, String writer, String content, String date) {
		this.no = no;
		this.subject = subject;
		this.writer = writer;
		this.content = content;
		this.date = date;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("no = ");
		sb.append(no);
		sb.append(", writer = ");
		sb.append(writer);
		sb.append(", content = ");
		sb.append(content);
		sb.append(", date = ");
		sb.append(date);
		return sb.toString();
	}
}
