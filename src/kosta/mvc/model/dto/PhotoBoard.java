package kosta.mvc.model.dto;

public class PhotoBoard extends Board {
	private String imgName;

	public PhotoBoard(int no, String subject, String writer, String content, String date,String imgName) {
		super(no, subject, writer, content, date);
		this.imgName=imgName;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(super.toString());
		sb.append(", imgName = ");
		sb.append(imgName);
		return sb.toString();
	}
	
}
