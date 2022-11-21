package com.dongjji.como.board.controller;

import com.dongjji.como.board.dto.*;
import com.dongjji.como.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/boards")
    public String createBoard(CreateBoardDto.Request request, Model model, String userEmail) {
        // TODO: 현재 로그인한 이메일 계정을 넣기
        CreateBoardDto.Response board = boardService.createBoard(request, userEmail);
        model.addAttribute("board", board);
        return "board/board-detail";
    }

    @PreAuthorize("hasRole('USEr')")
    @PatchMapping("/boards/{boardId}")
    public String updateBoard(
            @PathVariable Long boardId,
            UpdateBoardDto.Request request, Model model, String userEmail) {
        // TODO: 현재 로그인한 이메일 계정을 넣기
        UpdateBoardDto.Response board = boardService.updateBoard(boardId, request, userEmail);
        model.addAttribute("board", board);
        return "board/board-detail";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/boards/{boardId}")
    public String getBoard(@PathVariable Long boardId, Model model) {
        GetBoardDto.Response board = boardService.getBoard(boardId);
        model.addAttribute("board", board);
        return "board/board-detail";
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/boards/{boardId}")
    public String deleteBoard(@PathVariable Long boardId, String userEmail) {
        // TODO: 현재 로그인한 이메일 계정을 넣기
        boardService.deleteBoard(boardId, userEmail);
        return "redirect:/boards";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/boards/like/{boardId}")
    public String likeBoard(@PathVariable Long boardId, String userEmail) {
        // TODO: 현재 로그인한 이메일 계정을 넣기
        boardService.likeBoard(boardId, userEmail);
        return "redirect:/boards/" + boardId;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/boards/comments/{boardId}")
    public String commentBoard(@PathVariable Long boardId,
                               CommentBoardDto.Request request,
                               String userEmail) {
        // TODO: 현재 로그인한 이메일 계정을 넣기
        boardService.commentBoard(boardId, request, userEmail);
        return "redirect:/boards/" + boardId;
    }

    @PreAuthorize("hasRole('User')")
    @DeleteMapping("/boards/comment/{boardCommentId}")
    public String deleteComment(@PathVariable Long boardCommentId, String userEmail) {
        // TODO: 현재 로그인한 이메일 계정을 넣기
        Long boardId = boardService.deleteComment(boardCommentId, userEmail);
        return "redirect:/boards/" + boardId;
    }

    @PreAuthorize("hasRole('User')")
    @PostMapping("/boards/search")
    public String searchBoard(SearchBoardDto.Request request, Model model) {
        List<GetBoardDto.Response> boards = boardService.searchBoard(request);
        model.addAttribute("boards", boards);
        return "boards/boards";
    }

}
