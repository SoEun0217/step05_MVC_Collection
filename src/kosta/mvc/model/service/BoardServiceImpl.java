package kosta.mvc.model.service;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import kosta.mvc.model.dto.ArchiveBoard;
import kosta.mvc.model.dto.Board;
import kosta.mvc.model.dto.PhotoBoard;
import kosta.mvc.model.exception.DuplicateException;
import kosta.mvc.model.exception.InexistentException;

public class BoardServiceImpl implements BoardService {

	Map<String, Map<String, Board>> allBoardList = new TreeMap<String, Map<String, Board>>();
	private static BoardServiceImpl instance = new BoardServiceImpl();

	/**
	 * �ܺο��� ��ü���� ���ϵ��� private ������ ����� :�ܺ� properties���� �ε��ؼ� Board�� ������ Map�� �����ϴ� ������
	 * �ʱ�ġ ����
	 */
	private BoardServiceImpl() {
		Map<String, Board> archiveMap = new TreeMap<String, Board>();
		Map<String, Board> photoMap = new TreeMap<String, Board>();

		ResourceBundle rb = ResourceBundle.getBundle("kosta/mvc/model/service/archiveInfo");
		for (String key : rb.keySet()) {
			String value = rb.getString(key);
			// System.out.println(key+" = "+value);
			// value�� ���� �޸��� �������� �и��ؾ���
			String v[] = value.split(",");
			Board board = new ArchiveBoard(Integer.parseInt(v[0]), v[1], v[2], v[3], v[4], v[5],
					Integer.parseInt(v[6]));
			archiveMap.put(key, board);
		} // for�� ��

		rb = ResourceBundle.getBundle("kosta/mvc/model/service/photoInfo");
		for (String key : rb.keySet()) {
			String value = rb.getString(key);
			String v[] = value.split(",");
			Board board = new PhotoBoard(Integer.parseInt(v[0]), v[1], v[2], v[3], v[4], v[5]);
			photoMap.put(key, board);
		} // for�� ��

		// allBoardListd�� �ΰ��� map�� �����Ѵ�.
		allBoardList.put("archive", archiveMap);
		allBoardList.put("photo", photoMap);

	}

//	public static void main(String[] args) {
//		BoardServiceImpl.getInsatance();
//	}

	public static BoardServiceImpl getInsatance() {
		return instance;
	}

	@Override
	public Map<String, Map<String, Board>> getBoardList() throws InexistentException {
		if (allBoardList == null) {
			throw new InexistentException("�Խù��� �������� �ʽ��ϴ�.");
		}
		return allBoardList;
	}

	@Override
	public Map<String, Board> getBoardByKind(String kind) throws InexistentException {
		Map<String, Board> map = allBoardList.get(kind);
		if (map == null) {
			throw new InexistentException(kind + "������ �Խ����� �������� �ʽ��ϴ�.");
		} else if (map.isEmpty()) {// ������ ������ �Խù��� �������� �ʴ� ���
			throw new InexistentException(kind + "������ �Խ��ǿ� �Խù��� �����ϴ�.");
		}
		return map;
	}

	@Override
	public Board getBoardByNo(String kind, int no) throws InexistentException {
		Map<String, Board> map = allBoardList.get(kind);
		
		if(map==null||kind.isEmpty()) {
			throw new InexistentException(kind+"������ �Խ����� ���ų� ������ �������� �ʽ��ϴ�.");
		}
//		�� �ڵ�
//		if (map == null) {
//			throw new InexistentException(kind + "�Խ����� �������� �ʽ��ϴ�.");
//		}
		Board board = map.get(Integer.toString(no));
		if (board == null) {
			throw new InexistentException(kind+"������ "+no + "��ȣ�� �Խñ��� �������� �ʽ��ϴ�.");
		}
		return board;

	}

	@Override
	public void insertBoard(String kind, Board board) throws DuplicateException, InexistentException {
		if(!allBoardList.containsKey(kind)) {
			throw new InexistentException(kind+"������ �Խ����� �����Ƿ� ��� �� �� �����ϴ�.");
		}
		this.duplicateByNo(kind, board.getNo());//duplicateByNo����ϱ�!!
		if(this.duplicateByNo(kind, board.getNo()))
			throw new DuplicateException(board.getNo()+"�� ���� �̹� �����ϹǷ� ����� �� �����ϴ�.");
		
		allBoardList.get(kind).put(Integer.toString(board.getNo()),board);
		
//		�� �ڵ�			
//		Map<String, Board> map = allBoardList.get(kind);
//		if (map == null) {
//			throw new InexistentException(kind + "�Խ����� �������� �ʽ��ϴ�.");
//		}
//		for (String s : map.keySet()) {
//			Board board1 = map.get(s);
//			if (board1.getNo() == board.getNo()) {
//				throw new DuplicateException("�۹�ȣ�� �ߺ��Դϴ�.");
//			}
//		}
//		map.put(kind, board);
	}

	@Override
	public boolean duplicateByNo(String kind, int no) {
		Board board=allBoardList.get(kind).get(Integer.toString(no));//.���� �����ϱ� �޼ҵ� ü�ι���̴�.
		if(board==null)return false;//�ߺ��̴�.
//		Map<String,Board>map=allBoardList.get(kind);
//		if(map.containsKey(Integer.toString(no))) {
//			return false;
//		}
		return true;
	}

	@Override
	public void deleteBoard(String kind, int no) throws InexistentException {
		Map<String, Board> map =allBoardList.get(kind);
		if(map==null)
			throw new InexistentException(kind+"������ �߸��Ǿ� ���� �� �� �����ϴ�.");
		Board board=map.remove(Integer.toString(no));//remove�� ������ ��ü�� �����ϰ� �ȴ�.
		if(board==null)
			throw new InexistentException(no+"������ �������� �ʾҽ��ϴ�.");
//		Board board = this.getBoardByNo(kind, no);
//		map.remove(Integer.toString(no));
//		if (board == null) {
//			throw new InexistentException("�Խñ��� �������� �ʽ��ϴ�.");
//		}

	}

	// ���� �ۼ��� ����
	@Override
	public void updateBoard(Board board, String kind) throws InexistentException {
		Map<String, Board>kindMap=allBoardList.get(kind);
		if(kindMap==null||kindMap.isEmpty())
			throw new InexistentException(kind+"������ �߸��Ǿ��ų� �Խù��� ������ �����ϴ�.");
		
		Board dbBoard=kindMap.get(Integer.toString(board.getNo()));
		if(dbBoard==null)
			throw new InexistentException(board.getNo()+"��ȣ ������ ���� �� �� �����ϴ�.");
		
		//�����ϱ�
		if(board instanceof ArchiveBoard&&dbBoard instanceof ArchiveBoard) {
			//�ڷ�Խ��� ����
			dbBoard.setSubject(board.getSubject());
			dbBoard.setWriter(board.getWriter());
			dbBoard.setContent(board.getContent());
			
			//ĳ����
			ArchiveBoard dbABoard= (ArchiveBoard) dbBoard;
			ArchiveBoard dataABoard=(ArchiveBoard)board;
			dbABoard.setFileName(dataABoard.getFileName());
			dbABoard.setFileSize(dataABoard.getFileSize());
			
		}else if(board instanceof PhotoBoard&&dbBoard instanceof PhotoBoard) {
			//�ڷ�Խ��� ����
			dbBoard.setSubject(board.getSubject());
			dbBoard.setWriter(board.getWriter());
			dbBoard.setContent(board.getContent());
			
			//ĳ����
			PhotoBoard dbPBoard=(PhotoBoard)dbBoard;
			PhotoBoard dataPBoard=(PhotoBoard)board;
			dbPBoard.setImgName(dataPBoard.getImgName());
			
		}else {
			throw new InexistentException("Ÿ���� �߸��Ǿ� ������ �� �����ϴ�.");
		}
	}
		
	
//		Map<String, Board> map = this.getBoardByKind(kind);
//		int no = board.getNo();
//		Board databoard = this.getBoardByNo(kind, no);
//		if (map == null) {
//			throw new InexistentException(kind + "�� �߸��� �Խ��� �̸��Դϴ�");
//		}
//		if (databoard == null) {
//			throw new InexistentException(kind + "�� �߸��� �Խñ� ��ȣ�Դϴ�");
//		}
//		databoard.setSubject(board.getSubject());
//		databoard.setWriter(board.getWriter());
//		databoard.setContent(board.getContent());
//		if (board instanceof ArchiveBoard) {
//			ArchiveBoard acBoard = (ArchiveBoard) board;
//			acBoard.setFileName(acBoard.getFileName());
//			acBoard.setFileSize(acBoard.getFileSize());
//		}
//		if (board instanceof PhotoBoard) {
//			PhotoBoard phBoard = (PhotoBoard) board;
//			phBoard.setImgName(phBoard.getImgName());
//		}
//
//	}

}
