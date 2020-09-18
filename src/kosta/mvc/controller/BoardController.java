package kosta.mvc.controller;

import java.util.Map;

import kosta.mvc.model.dto.Board;
import kosta.mvc.model.exception.DuplicateException;
import kosta.mvc.model.exception.InexistentException;
import kosta.mvc.model.service.BoardService;
import kosta.mvc.model.service.BoardServiceImpl;
import kosta.mvc.view.FailView;
import kosta.mvc.view.SuccessView;

public class BoardController {
	private static BoardService boardService=BoardServiceImpl.getInsatance();

	public static void getAllBoard() {
		try {
			SuccessView.printBoard(boardService.getBoardList());
		}catch(InexistentException e) {
			FailView.errorMessage(e.getMessage());
		}

	}

	public static void getBoardByKind(String kind) {
		try {
		Map<String, Board> map=boardService.getBoardByKind(kind);
		SuccessView.printBoardByKind(kind,map);
		}catch(InexistentException e) {
			FailView.errorMessage(e.getMessage());
		}
	}

	public static void getBoardByNo(String kind, int no) {
		try {
		Board board=boardService.getBoardByNo(kind, no);
		SuccessView.printBoardByNo(board);
		}catch(InexistentException e) {
			FailView.errorMessage(e.getMessage());
		}
	}

	public static void insertBoard(String kind, Board board) {
		//Exception을 두개 받을때 1)catch 2개 2)Exception e로 같이 묶어버리기 3)|연산자 사용하기
		try {
		boardService.insertBoard(kind, board);
		SuccessView.printMessage("등록되었습니다.");
	}catch(InexistentException | DuplicateException e) {
		FailView.errorMessage(e.getMessage());
	}
//	}catch(DuplicateException e) {
//		FailView.errorMessage(e.getMessage());
//		
//	}
}

	public static void deleteBoard(String kind, int no) {
		try {
		boardService.deleteBoard(kind, no);
		SuccessView.printMessage("삭제되었습니다.");
		}catch(InexistentException e) {
			FailView.errorMessage(e.getMessage());
		}
	}

	public static void updateBoard(Board board, String kind) {
		try {
		boardService.updateBoard(board, kind);
		SuccessView.printMessage("수정완료되었습니다.");
		}catch(InexistentException e) {
		FailView.errorMessage(e.getMessage());
		}
	}
	

}
