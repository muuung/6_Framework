package edu.kh.project.member.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.member.model.vo.Member;

@Repository // DB 연결하는 역할 + bean 등록
public class AjaxDAO {

    // SqlsessionTemplate : 커넥션 + 마이바티스 + 스프링 TX 제어
    
    @Autowired
    private SqlSessionTemplate sqlSession;

    public int emailDupCheck(String memberEmail) {
        return sqlSession.selectOne("ajaxMapper.emailDupCheck", memberEmail);
    }

    public int nicknameDupCheck(String memberNickname) {
        return sqlSession.selectOne("ajaxMapper.nicknameDupCheck", memberNickname);
    }

    public Member selectEmail(String email) {
        return sqlSession.selectOne("ajaxMapper.selectEmail", email);
    }

    public List<Member> selectMemberList() {
        // selectList() : 조회결과의 각 행을 resultType 또는 resultMap에 맞는
        //                VO 객체에 담아 List에 추가하여 반환
        return sqlSession.selectList("ajaxMapper.selectMemberList");
    }
}