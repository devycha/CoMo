package com.dongjji.como.board.dto;

import com.dongjji.como.board.entity.Board;
import com.dongjji.como.board.entity.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetBoardDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String title;
        private String description;
        private boolean isPublic;
        private String author;

        private int likes;
        private List<BoardComment> comments;

        public static Response fromEntity(Board board) {
            return Response.builder()
                    .title(board.getTitle())
                    .description(board.getDescription())
                    .isPublic(board.isPublic())
                    .author(board.getAuthor())
                    .build();
        }
        public static Response fromEntity(Board board, List<BoardComment> comments, int likes) {
            return Response.builder()
                    .title(board.getTitle())
                    .description(board.getDescription())
                    .isPublic(board.isPublic())
                    .author(board.getAuthor())
                    .comments(comments)
                    .likes(likes)
                    .build();
        }
    }
}
