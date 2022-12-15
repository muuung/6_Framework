package edu.kh.project.board.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.vo.Board;

public interface BoardService {

    List<Map<String, Object>> selectBoardType();

    Map<String, Object> selectBoardList(int boardCode, int cp);

    Board selectBoardDetail(int boardNo);

    int updateReadCount(int boardNo);

    int boardLikeCheck(Map<String, Object> map);

    int boardLikeUp(Map<String, Object> paramMap);

    int boardLikeDown(Map<String, Object> paramMap);

    int boardDelete(int boardNo);

    int boardWrite(Board board, List<MultipartFile> imageList, String webPath, String folderPath) throws IOException;

    int boardUpdate(Board board, List<MultipartFile> imageList, String webPath, String folderPath, String deleteList) throws Exception;

    Map<String, Object> selectBoardList(Map pm, int cp);

    List<String> selectImageList();
}