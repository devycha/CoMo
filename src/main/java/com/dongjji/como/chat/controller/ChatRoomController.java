package com.dongjji.como.chat.controller;

import com.dongjji.como.chat.dto.ChatRoomDto;
import com.dongjji.como.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    private String getCurrentUserEmail(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        log.warn(authentication.getPrincipal().toString());
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    @GetMapping("/chat/my-chats/{chatRoomId}")
    public String getChatByChatRoomId(@PathVariable long chatRoomId, Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/user/login";
        }

        String email = getCurrentUserEmail(authentication);
        ChatRoomDto.Response chatRoom = chatRoomService.verifyChatAuth(email, chatRoomId);

        model.addAttribute("chatRoom", chatRoom);
        return "chat/chat";
    }

    @GetMapping("/chat/my-chats")
    public String getMyChats(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/user/login";
        }
        String email = getCurrentUserEmail(authentication);
        List<ChatRoomDto.Response> chatRooms = chatRoomService.getMyChats(email);
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("myEmail", email);

        return "chat/my-chats";
    }

    @PostMapping("/chat/my-chats")
    public String createNewChat(Authentication authentication, ChatRoomDto.Request createChatRequest) {
        if (authentication == null) {
            return "redirect:/user/login";
        }

        String capEmail = getCurrentUserEmail(authentication);
        long chatRoomId = chatRoomService.createNewChat(capEmail, createChatRequest.getInvitedEmail());

        return "redirect:/chat/my-chats/" + chatRoomId;
    }

    @DeleteMapping("/chat/my-chats/{chatRoomId}")
    public String deleteChatRoom(@PathVariable long chatRoomId, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/user/login";
        }

        String email = getCurrentUserEmail(authentication);
        chatRoomService.deleteChatRoom(chatRoomId, email);

        return "redirect:/chat/my-chats";
    }

    @DeleteMapping("/chat/my-chats/out/{chatRoomId}")
    public String getOutChatRoom(@PathVariable long chatRoomId, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/user/login";
        }

        String email = getCurrentUserEmail(authentication);
        chatRoomService.getOutChatRoom(chatRoomId, email);

        return "redirect:/chat/my-chats";
    }


}
