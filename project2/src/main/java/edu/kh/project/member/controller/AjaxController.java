package edu.kh.project.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import edu.kh.project.member.model.service.AjaxService;
import edu.kh.project.member.model.vo.Member;

@Controller
public class AjaxController {
    
    @Autowired
    private AjaxService service;
    
    @GetMapping("/emailDupCheck")
    @ResponseBody
    public int emailDupCheck(String memberEmail) {
//        System.out.println(memberEmail);
        
        int result = service.emailDupCheck(memberEmail);
            
        // @ResponseBody 어노테이션 덕분에
        // result가 View Resolver로 전달되지 않고
        // 호출했던 ajax 함수로 반환됨
        return result;
    }
    
    @GetMapping("/nicknameDupCheck")
    @ResponseBody
    public int nicknameDupCheck(String memberNickname) {
        
        int result = service.nicknameDupCheck(memberNickname);
        
        return result;
    }
    
    // 이메일로 회원 정보 조회
    @PostMapping("/selectEmail")
    @ResponseBody
    public String selectEmail(String email) {
        
        Member member = service.selectEmail(email);
        
        System.out.println(member);
        
        // JSON 형식으로 Member 객체 작성
        // {"memberEmail" : "user01@kh.or.kr", "memberNickname" : "유저일"}
        
//        String result = "{\"memberEmail\" : \"user01@kh.or.kr\", \"memberNickname\" : \"유저일\"}";
//        return result;
        
        return new Gson().toJson(member);
    }

    // 이메일로 회원 정보 조회(jackson-databind 활용)
//    @PostMapping("/selectEmail")
//    @ResponseBody
//    public Member selectEmail(String email) {
//        
//        // jackson이란?
//        // JSON <-> Java 객체 <-> JSON
//        Member member = service.selectEmail(email);
//        
// 
//        return member;
//    }
    
    // 회원 목록 조회
    @GetMapping("/selectMemberList")
    @ResponseBody
    public String selectMemberList(){
        
        List<Member> memberList = service.selectMemberList();
        
        // 객체 1개를 표현 == JSON
        // 객체 여러 개가 담긴 배열 == JSONArray
        //
        
        return new Gson().toJson(memberList);
    }
}
