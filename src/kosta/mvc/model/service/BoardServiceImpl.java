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
	 * 외부에서 객체생성 못하도록 private 생성자 만들기 :외부 properties파일 로딩해서 Board로 만든후 Map에 저장하는 데이터
	 * 초기치 세팅
	 */
	private BoardServiceImpl() {
		Map<String, Board> archiveMap = new TreeMap<String, Board>();
		Map<String, Board> photoMap = new TreeMap<String, Board>();

		ResourceBundle rb = ResourceBundle.getBundle("kosta/mvc/model/service/archiveInfo");
		for (String key : rb.keySet()) {
			String value = rb.getString(key);
			// System.out.println(key+" = "+value);
			// value의 값을 콤마를 기준으로 분리해야함
			String v[] = value.split(",");
			Board board = new ArchiveBoard(Integer.parseInt(v[0]), v[1], v[2], v[3], v[4], v[5],
					Integer.parseInt(v[6]));
			archiveMap.put(key, board);
		} // for문 끝

		rb = ResourceBundle.getBundle("kosta/mvc/model/service/photoInfo");
		for (String key : rb.keySet()) {
			String value = rb.getString(key);
			String v[] = value.split(",");
			Board board = new PhotoBoard(Integer.parseInt(v[0]), v[1], v[2], v[3], v[4], v[5]);
			photoMap.put(key, board);
		} // for문 끝

		// allBoardListd에 두개의 map을 저장한다.
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
			throw new InexistentException("게시물이 존재하지 않습니다.");
		}
		return allBoardList;
	}

	@Override
	public Map<String, Board> getBoardByKind(String kind) throws InexistentException {
		Map<String, Board> map = allBoardList.get(kind);
		if (map == null) {
			throw new InexistentException(kind + "유형의 게시판은 존재하지 않습니다.");
		} else if (map.isEmpty()) {// 유형은 맞지만 게시물이 존재하지 않는 경우
			throw new InexistentException(kind + "유형의 게시판에 게시물이 없습니다.");
		}
		return map;
	}

	@Override
	public Board getBoardByNo(String kind, int no) throws InexistentException {
		Map<String, Board> map = allBoardList.get(kind);
		
		if(map==null||kind.isEmpty()) {
			throw new InexistentException(kind+"유형의 게시판이 없거나 정보가 존재하지 않습니다.");
		}
//		내 코드
//		if (map == null) {
//			throw new InexistentException(kind + "게시판은 존재하지 않습니다.");
//		}
		Board board = map.get(Integer.toString(no));
		if (board == null) {
			throw new InexistentException(kind+"유형의 "+no + "번호의 게시글은 존재하지 않습니다.");
		}
		return board;

	}

	@Override
	public void insertBoard(String kind, Board board) throws DuplicateException, InexistentException {
		if(!allBoardList.containsKey(kind)) {
			throw new InexistentException(kind+"유형의 게시판은 없으므로 등록 할 수 없습니다.");
		}
		this.duplicateByNo(kind, board.getNo());//duplicateByNo사용하기!!
		if(this.duplicateByNo(kind, board.getNo()))
			throw new DuplicateException(board.getNo()+"번 글은 이미 존재하므로 등록할 수 없습니다.");
		
		allBoardList.get(kind).put(Integer.toString(board.getNo()),board);
		
//		내 코드			
//		Map<String, Board> map = allBoardList.get(kind);
//		if (map == null) {
//			throw new InexistentException(kind + "게시판은 존재하지 않습니다.");
//		}
//		for (String s : map.keySet()) {
//			Board board1 = map.get(s);
//			if (board1.getNo() == board.getNo()) {
//				throw new DuplicateException("글번호가 중복입니다.");
//			}
//		}
//		map.put(kind, board);
	}

	@Override
	public boolean duplicateByNo(String kind, int no) {
		Board board=allBoardList.get(kind).get(Integer.toString(no));//.으로 연결하기 메소드 체인방식이다.
		if(board==null)return false;//중복이다.
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
			throw new InexistentException(kind+"유형이 잘못되어 삭제 할 수 없습니다.");
		Board board=map.remove(Integer.toString(no));//remove는 삭제될 객체를 리턴하게 된다.
		if(board==null)
			throw new InexistentException(no+"오류로 삭제되지 않았습니다.");
//		Board board = this.getBoardByNo(kind, no);
//		map.remove(Integer.toString(no));
//		if (board == null) {
//			throw new InexistentException("게시글이 존재하지 않습니다.");
//		}

	}

	// 제목 작성자 내용
	@Override
	public void updateBoard(Board board, String kind) throws InexistentException {
		Map<String, Board>kindMap=allBoardList.get(kind);
		if(kindMap==null||kindMap.isEmpty())
			throw new InexistentException(kind+"유형이 잘못되었거나 게시물의 정보가 없습니다.");
		
		Board dbBoard=kindMap.get(Integer.toString(board.getNo()));
		if(dbBoard==null)
			throw new InexistentException(board.getNo()+"번호 오류로 수정 할 수 없습니다.");
		
		//수정하기
		if(board instanceof ArchiveBoard&&dbBoard instanceof ArchiveBoard) {
			//자료게시판 수정
			dbBoard.setSubject(board.getSubject());
			dbBoard.setWriter(board.getWriter());
			dbBoard.setContent(board.getContent());
			
			//캐스팅
			ArchiveBoard dbABoard= (ArchiveBoard) dbBoard;
			ArchiveBoard dataABoard=(ArchiveBoard)board;
			dbABoard.setFileName(dataABoard.getFileName());
			dbABoard.setFileSize(dataABoard.getFileSize());
			
		}else if(board instanceof PhotoBoard&&dbBoard instanceof PhotoBoard) {
			//자료게시판 수정
			dbBoard.setSubject(board.getSubject());
			dbBoard.setWriter(board.getWriter());
			dbBoard.setContent(board.getContent());
			
			//캐스팅
			PhotoBoard dbPBoard=(PhotoBoard)dbBoard;
			PhotoBoard dataPBoard=(PhotoBoard)board;
			dbPBoard.setImgName(dataPBoard.getImgName());
			
		}else {
			throw new InexistentException("타입이 잘못되어 수정할 수 없습니다.");
		}
	}
		
	
//		Map<String, Board> map = this.getBoardByKind(kind);
//		int no = board.getNo();
//		Board databoard = this.getBoardByNo(kind, no);
//		if (map == null) {
//			throw new InexistentException(kind + "는 잘못된 게시판 이름입니다");
//		}
//		if (databoard == null) {
//			throw new InexistentException(kind + "는 잘못된 게시글 번호입니다");
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
