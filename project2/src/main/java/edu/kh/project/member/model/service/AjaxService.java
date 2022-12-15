package edu.kh.project.member.model.service;

import java.util.List;

import org.springframework.stereotype.Controller;

import edu.kh.project.member.model.vo.Member;

// 서비스 인터페이스를 만든 이유
// 설계, 유지보수성 향상, AOP 사용 목적

public interface AjaxService {

    /**
     * 이메일 중복 검사 서비스
     * @param memberEmail
     * @return
     */
    int emailDupCheck(String memberEmail);

    /**
     * 닉네임 중복 검사 서비스
     * @param memberNickname
     * @return
     */
    int nicknameDupCheck(String memberNickname);

    /**
     * 이메일로 회원 정보 조회
     * @param email
     * @return
     */
    Member selectEmail(String email);

    
    /**
     * 회원 목록 조회
     * @return
     */
    List<Member> selectMemberList();
}