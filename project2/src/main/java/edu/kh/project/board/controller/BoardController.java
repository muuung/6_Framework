package edu.kh.project.board.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.vo.Board;
import edu.kh.project.common.Util;
import edu.kh.project.member.model.vo.Member;

@Controller
public class BoardController {
    
    @Autowired
    private BoardService service;
    
    // 특정 게시판 목록 조회
    // /board/1
    // /board/2
    // /board/3
    // /board/4
    
    // @pathVariable 사용!
    // URL 경로에 있는 값을 파라미터(변수)로 사용할 수 있게 해주는 어노테이션
    // + 자동으로 request scope로 등록되어 EL 구문으로 jsp에서 출력 가능
    
    // 요청주소?K=V&K=V&K=V
    // queryString(쿼리스트링)
    // 요청주소에 값을 담아서 전달할 때 사용하는 문자열
    
    @GetMapping("/board/{boardCode}")
    public String selectBoardList(@PathVariable("boardCode") int boardCode,
                                  Model model,
                                  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
                                  @RequestParam Map pm) {
        
        // 검색이 아닌 경우
        if(pm.get("key") == null) {
            Map<String, Object> map = service.selectBoardList(boardCode, cp);
            model.addAttribute("map", map); // request scope 세팅
        
        // 검색인 경우
        } else {
            
            // 게시판 번호를 pm에 추가
            pm.put("boardCode", boardCode);
            Map<String, Object> map = service.selectBoardList(pm,  cp);
            model.addAttribute("map", map); // request scope 세팅 
        }
        
        return "board/boardList"; // forward
    }
    
    // 게시글 상세조회
    @GetMapping("/board/{boardCode}/{boardNo}")
    public String boardDetail(@PathVariable("boardNo") int boardNo,
                              @PathVariable("boardCode") int boardCode,
                              Model model,
                              HttpServletRequest req,
                              HttpServletResponse resp,
                              @SessionAttribute(value="loginMember", required=false) Member loginMember) throws ParseException {
                              // Session에 loginMember가 없으면 null
        
        // 게시글 상세 조회 서비스 호출
        Board board = service.selectBoardDetail(boardNo);
        
        // 좋아요 수, 좋아요 여부
        if(board != null) {
            
            // BOARD_LIKE 테이블에
            // 게시글 번호, 로그인한 회원 번호가 일치하는 행이 있는지 확인
            
            if(loginMember != null) { // 로그인 상태인 경우
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("boardNo", boardNo);
                map.put("memberNo", loginMember.getMemberNo());
                
                int result = service.boardLikeCheck(map);
                
                if(result > 0) { // 좋아요가 되어있는 경우
                    model.addAttribute("likeCheck", "on");
                    
                }
            }
        }
        
        // 조회수 증가(쿠키를 이용해서 해당 IP 당 하루 한 번)
        
        // 게시글 상세 조회 성공 시 조회 수 증가
        if(board != null) {
            
            // 컴퓨터 1대 당 게시글마다 1일 1번씩만 조회수 증가
            // -> 쿠키 이용
            
            // Cookie
            // 사용되는 경로, 수명
            
            // 경로 지정 시 해당 경로 또는 이하 요청을 보낼 때
            // 쿠키 파일을 서버로 같이 보냄
            
            // 쿠키에 "readBoardNo"를 얻어와
            // 현재 조회하려는 게시글 번호가 없으면
            // 조회수 1 증가 후 쿠키에 게시글 번호 추가
            
            // 만약에 있으면 조회수 증가 X
            
            // 쿠키 얻어오기
            Cookie[] cookies = req.getCookies();
            
            // 쿠키 중 "readBoardNo"가 있는지 확인
            Cookie c = null;
            
            // 쿠키가 있을 경우
            if(cookies != null) {
                
                for(Cookie temp : cookies) {
                    // 얻어온 쿠키의 이름이 "readBoardNo"인 경우
                    if(temp.getName().equals("readBoardNo")) {
                        c = temp;
                    }
                }
            }
            
            // 조회수 증가 serivce 호출 결과 저장
            int result = 0;
            
            // "readBoardNo" 쿠키가 없을 경우 == 오늘 상세조회 한 번도 X
            if(c == null) {
                result = service.updateReadCount(boardNo);
                
                // boardNo 게시글을 상세조회 했음을 쿠키에 기록
                // 톰캣 8.5 이상부터 쿠키의 값으로 ; , = (공백) 사용 불가
                c = new Cookie("readBoardNo", "|" + boardNo + "|");
                
            // "readBoardNo" 쿠키가 있을 경우
            } else {
                
                // c.getValue() : "readBoardNo" 쿠키에 저장된 값 (|1990|)
                // 쿠키에 저장된 값 중 "|게시글번호|" 가 존재하는지 확인
                
                // 존재하지 않는 경우 == 오늘 처음 조회하는 게시글 번호
                if(c.getValue().indexOf("|"+ boardNo +"|") == -1) {
                    
                    // 조회수 증가
                    result = service.updateReadCount(boardNo);
                    
                    // 현재 조회한 게시글 번호를 쿠키에 값으로 추가
                    c.setValue(c.getValue() + "|" + boardNo + "|");
                } 
            }
            
            // 조회수 증가 성공 시 DB와 조회된 Board 조회수 동기화
            if(result > 0) {
                board.setReadCount(board.getReadCount()+1);
                
                // 쿠키 적용 경로, 수명 설정 후 클라이언트에게 전달
                // localhost/(최상위 경로 /) 이하로 적용
                c.setPath("/");
                
                // Date : 날짜용 객체
                // Calendar : Date 업글이드 객체
                // SimpleDateFormat : 날짜를 원하는 형태의 문자열로 반환
                
                // 오늘 3시 59분 59초까지 남은 시간을 초 단위로 구하기
                
                Date a = new Date(); // 현재 시간
                
                Calendar cal = Calendar.getInstance();
                
                //cal.add(단위, 추가할 값);
                cal.add(cal.DATE, 1); // 날짜에 1 추가
                
                // 기준 시간 : 1970년 1월 1일 09시 0분 0초
                // new Date(ms) : 기준 시간 + ms 만큼 지난 시간
                
                // SimpleDateFormat을 이용해서 cal 날짜 중 시, 분, 초를 0:0:0 바꿈
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
                // 하루 증가한 내일 날짜의 ms 값을 이용해서 Date 객체 생성
                Date temp = new Date(cal.getTimeInMillis());
                
                Date b = sdf.parse(sdf.format(temp));
                
                // 내일 자정 ms - 현재 시간 ms
                long diff = b.getTime() - a.getTime();
                
                c.setMaxAge((int)(diff / 1000));
                
                resp.addCookie(c); // 쿠키를 클라이언트에게 전달
            }
        }
        
        model.addAttribute("board", board);
        
        return "board/boardDetail";
    }
    
    // 좋아요 수 증가(INSERT)
    @GetMapping("/boardLikeUp")
    @ResponseBody
    public int boardLikeUp(@RequestParam Map<String, Object> paramMap) {
        // @RequestParam Map<String, Object>
        // 요청 시 전달된 파라미터를 하나의 Map으로 반환
        return service.boardLikeUp(paramMap);
    }
    
    // 좋아요 수 감소(DELETE)
    @GetMapping("/boardLikeDown")
    @ResponseBody
    public int boardLikeDown(@RequestParam Map<String, Object> paramMap) {
        return service.boardLikeDown(paramMap);
    }
    
    // 게시글 삭제
    @GetMapping("/board/{boardCode}/{boardNo}/delete")
    public String boardDelete(@RequestHeader("referer") String referer,
                              @PathVariable("boardNo") int boardNo,
                              @PathVariable("boardCode") int boardCode,
                              RedirectAttributes ra) {
        
        // 게시글 번호를 이용해서 게시글을 삭제(BOARD_DEL_FL = 'Y' 수정)
        int result = service.boardDelete(boardNo);
        
        String path = null;
        String message = null;
        
        // 성공 시 : "삭제되었습니다" 메시지 전달
        // 헤당 게시판 목록 1페이지로 리다이렉트
        if(result > 0) {
//          path = "/board/{boardCode}";
            path = "/board/" + boardCode;
            message = "삭제되었습니다";
            
        // 실패 시 : "게시글 삭제 실패" 메세지 전달
        //요청 이전 주소(referer)로 리다이렉트
        } else {
            path = referer;
            message = "게시글 삭제 실패";
        }
        
        ra.addFlashAttribute("message", message);
        
        return "redirect:" + path;
    }
    
    // 게시글 작성 페이지 이동
    @GetMapping("/write/{boardCode}")
    public String boardWrite(@PathVariable("boardCode") int board) {
        return "board/boardWrite";
                // 서블릿 콘텍스트뷰 리졸버에 지정되어 있는 prefix suffix 빼고 작성
    }
    
    // 게시글 삽입
    @PostMapping("/write/{boardCode}")
    public String boardWrite(Board board,
                             @RequestParam(value="images", required=false) List<MultipartFile> imageList,
                             @SessionAttribute("loginMember") Member loginMember,
                             @PathVariable("boardCode") int boardCode,
                             RedirectAttributes ra, HttpSession session,
                             @RequestHeader("referer") String referer) throws IOException {
        
        // 1. 게시글만 삽입
        // 1) CSS(크로스 사이트 스크립트 공격), 개행문자 처리
        board.setBoardTitle(Util.XSSHandling(board.getBoardTitle()));
        board.setBoardContent(Util.XSSHandling(board.getBoardContent()));
        
        board.setBoardContent(Util.newLineHandling( board.getBoardContent()));
        
        // 1. boardCode를 board 객체에 세팅 (board에 boardCode 필드 추가)
        board.setBoardCode(boardCode);
        
        // 2. 로그인한 회원의 번호를 board 객체에 세팅
        board.setMemberNo(loginMember.getMemberNo());
        
        // 3. 업로드된 파일의 웹 접근경로/서버 내부 경로 준비
        String webPath = "/resources/images/board/";
        
        // /resources/images/board/ 까지의 컴퓨터 저장 경로 반환
        String folderPath = session.getServletContext().getRealPath(webPath);
        
        // 4. 게시글 삽입 서비스 호출
        int boardNo = service.boardWrite(board, imageList, webPath, folderPath);
        
        String message = null;
        String path = null;
        
        if(boardNo > 0) {
            message = "게시글이 등록되었습니다.";
            path = "/board/" + boardCode + "/" + boardNo;
            
        } else {
            message = "게시글 작성 실패";
            path = referer;
        }
        
        ra.addFlashAttribute("message", message);
        
        return "redirect:" + path;
    }
    
    // 게시글 수정 화면 전환
    @GetMapping("/board/{boardCode}/{boardNo}/update")
    public String boardUpdate(@PathVariable("boardCode") int boardCode,
                              @PathVariable("boardNo") int boardNo,
                              Model model) {
        
        Board board = service.selectBoardDetail(boardNo);
        
        // 개행문자 처리 해제
        board.setBoardContent(Util.newLineClear(board.getBoardContent()));
        
        model.addAttribute("board", board);
        
        return "board/boardUpdate";
    }
    
    // 게시글 수정
    @PostMapping("/board/{boardCode}/{boardNo}/update")
    public String boardUpdate(@PathVariable("boardCode") int boardCode, // 게시판 번호
                              @PathVariable("boardNo") int boardNo, // 수정할 게시글 번호
                              @RequestParam(value="cp", required=false, defaultValue="1") int cp, // 현재 페이지 번호
                              @RequestParam(value="deleteList", required=false) String deleteList, // 삭제된 이미지 리스트
                              @RequestParam(value="images", required=false) List<MultipartFile> imageList, // 업로드
                              @RequestHeader("referer") String referer, // 이전 요청 주소
                              HttpSession session, // 서버 파일 저장경로 얻기 용도
                              RedirectAttributes ra, // 리다이렉트 시 응답 메세지 전달용
                              Board board) throws Exception { // boardTitle, boardContent (커맨드 객체)
        
        // 1. board 객체에 boardNo 세팅
        board.setBoardNo(boardNo);
        
        // 2. 이미지 저장 경로 얻어오기
        String webPath = "/resources/images/board/";
        String folderPath = session.getServletContext().getRealPath(webPath);
        
        // 3. 게시글 수정 서비스 호출
        int result = service.boardUpdate(board, imageList, webPath, folderPath, deleteList);
        
        // 4. 서비스 결과에 따른 응답 제어
        String path = null;
        String message = null;
        
        if(result > 0) {
            path = "/board/" + boardCode + "/" + boardNo + "?cp=" + cp;
            message = "게시글이 수정되었습니다.";
            
        } else {
            path = referer;
            message = "게시글 수정 실패"; // result < 0 이거나 return 0; 일 때 출력
        }
        
        ra.addFlashAttribute("message", message);
        
        return "redirect:" + path;
    }
}