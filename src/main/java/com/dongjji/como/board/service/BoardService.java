package com.dongjji.como.board.service;

import com.dongjji.como.board.dto.*;
import com.dongjji.como.board.entity.Board;
import com.dongjji.como.board.entity.BoardComment;
import com.dongjji.como.board.entity.BoardLike;
import com.dongjji.como.board.repository.BoardCommentRepository;
import com.dongjji.como.board.repository.BoardLikeRepository;
import com.dongjji.como.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardCommentRepository boardCommentRepository;

    public CreateBoardDto.Response createBoard(CreateBoardDto.Request request, String userEmail) {
        Board board = boardRepository.save(
                Board.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .isPublic(request.isPublic())
                        .author(userEmail).build()
        );

        return CreateBoardDto.Response.fromEntity(board);
    }

    public UpdateBoardDto.Response updateBoard(Long boardId, UpdateBoardDto.Request request, String userEmail) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 게시글입니다.")
        );

        if (!board.getAuthor().equals(userEmail)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        board.update(request);
        boardRepository.save(board);
        return UpdateBoardDto.Response.fromEntity(board);
    }

    public GetBoardDto.Response getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 게시글입니다.")
        );

        List<BoardComment> comments = boardCommentRepository.findAllByBoardId(boardId);
        int likes = boardLikeRepository.countAllByBoardId(boardId);

        return GetBoardDto.Response.fromEntity(board, comments, likes);
    }

    public void deleteBoard(Long boardId, String userEmail) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 게시글입니다.")
        );

        if (!board.getAuthor().equals(userEmail)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }

    public void likeBoard(Long boardId, String userEmail) {
        boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 게시글입니다.")
        );

        boolean alreadyLiked = boardLikeRepository.existsBoardLikeByBoardIdAndUserEmail(boardId, userEmail);
        if (alreadyLiked) {
            throw new RuntimeException("이미 좋아요를 누른 게시글입니다.");
        }

        boardLikeRepository.save(BoardLike.builder()
                .boardId(boardId)
                .userEmail(userEmail)
                .build()
        );
    }

    public void commentBoard(Long boardId, CommentBoardDto.Request request, String userEmail) {
        boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 게시글입니다.")
        );

        boardCommentRepository.save(
                BoardComment.builder()
                        .boardId(boardId)
                        .comment(request.getComment())
                        .author(userEmail).build()
        );
    }

    public Long deleteComment(Long boardCommentId, String userEmail) {
        BoardComment boardComment = boardCommentRepository.findById(boardCommentId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 댓글입니다.")
        );

        if (!boardComment.getAuthor().equals(userEmail)) {
            throw new RuntimeException("접근 권한이 없습니다");
        }

        Long boardId = boardComment.getBoardId();
        boardCommentRepository.delete(boardComment);
        return boardId;
    }

    public List<GetBoardDto.Response> searchBoard(SearchBoardDto.Request request) {
        return boardRepository.findAllByTitleLikeOrDescriptionLike(request.getSearchValue(), request.getSearchValue())
                .stream().map(GetBoardDto.Response::fromEntity).collect(Collectors.toList());
    }
}
