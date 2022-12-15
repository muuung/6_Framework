package edu.kh.project.member.model.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.vo.Member;

// 설계 + 유지보수 + AOP
public interface MyPageService {
    
    /**
     * 회원 정보 수정 서비스
     * @param inputMember
     * @return result
     */
    public abstract int updateInfo(Member inputMember);

    public abstract int changePw(Map<String, Object> paramMap);

    public abstract int memberDelete(int memberNo, String memberPw);

    public abstract int updateProfile(String webPath, String filePath, MultipartFile profileImage, Member loginMember) throws Exception;
}