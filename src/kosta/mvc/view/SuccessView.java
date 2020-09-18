package kosta.mvc.view;

import java.util.Map;

import kosta.mvc.model.dto.Board;

public class SuccessView {
	
	public static void printBoard(Map<String, Map<String, Board>> allBoard) {
		for(String s:allBoard.keySet()) {
			System.out.println("***"+s+"Board의 모든 게시물 LIST***");
			Map<String, Board>eachBoard=allBoard.get(s);
			for(String str:eachBoard.keySet()) {
				Board oneBoard=eachBoard.get(str);
				System.out.println("key = "+str+" [ "+oneBoard+" ] ");
			}
			System.out.println();
		}
		
		
	}
	
	public static void printBoardByKind(String kind,Map<String,Board> board) {
		System.out.println("*********"+kind+"유형의 게시물 LIST******");
		for(String s:board.keySet()) {
			Board eachBoard=board.get(s);
			System.out.println(s+" : "+eachBoard);
		}
	}
	
	public static void printBoardByNo(Board board) {
		System.out.println(board);
		
	}
	
	public static void printMessage(String message) {
		System.out.println(message);
		
	}

}
