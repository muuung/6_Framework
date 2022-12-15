package edu.kh.project.chatting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import edu.kh.project.chatting.model.service.ChattingService;
import edu.kh.project.chatting.model.vo.ChattingRoom;
import edu.kh.project.chatting.model.vo.Message;
import edu.kh.project.member.model.vo.Member;

@Controller
public class ChattingController {
    
    @Autowired
    private ChattingService service;
    
    @GetMapping("/chatting/enter")
    public String chattingEnter(int targetNo, RedirectAttributes ra,
            @SessionAttribute("loginMember") Member loginMember) {
     
        Map<String, Integer> map = new HashMap<String, Integer>();
        
        // 상대방 번호
        map.put("targetNo", targetNo);
        
        // 로그인한 사람 번호
        map.put("loginMemberNo", loginMember.getMemberNo());
        
        int chattingNo = service.checkChattingNo(map);
        
        if(chattingNo == 0) {
            chattingNo = service.createChattingRoom(map);
        }
        
        ra.addFlashAttribute("chattingNo", chattingNo);
        System.out.println(chattingNo);
        
        return "redirect:/chatting";
    }
    
    @GetMapping("/chatting")
    public String chatting(@SessionAttribute("loginMember") Member loginMember, Model model) {
        
        List<ChattingRoom> roomList = service.selectRoomList(loginMember.getMemberNo());
        model.addAttribute("roomList", roomList);
        return "chatting/chatting";
    }
    
    @GetMapping("/chatting/selectMessage")
    @ResponseBody
    public String selectMessageList(@RequestParam Map<String, Object> paramMap) {
        System.out.println(paramMap);
        List<Message> messageList = service.selectMessageList(paramMap);
        return new Gson().toJson(messageList);
    }
    
    @GetMapping("/chatting/roomList")
    @ResponseBody
    public String selectRoomList(int memberNo) {
        
        List<ChattingRoom> roomList = service.selectRoomList(memberNo);
        return new Gson().toJson(roomList);
    }
    
    @GetMapping("/chatting/updateReadFlag")
    @ResponseBody
    public int updateReadFlag(@RequestParam Map<String, Object> paramMap) {
        return service.updateReadFlag(paramMap);
    }
}