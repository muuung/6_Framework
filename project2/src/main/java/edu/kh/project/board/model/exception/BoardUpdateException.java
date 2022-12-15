package edu.kh.project.board.model.exception;

import java.io.IOException;

// 사용자 정의 예외
// 자바에서 제공하지 않는 예외가 필요한 경우 만드는 예외 클래스

// 작성법 : 원하는 예외를 하나 상속받고, 생성자만 구현
// Unchecked : 꼭 예외처리 필요 X
public class BoardUpdateException extends RuntimeException {
   
    public BoardUpdateException() {
        // 부모 생성자 안에 메세지를 적을 경우
        // 에러가 발생했을 때 콘솔창에 메세지를 출력
        super("게시글 수정 예외");
    }

    public BoardUpdateException(String message) {super(message);}
}