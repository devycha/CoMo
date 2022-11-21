package com.dongjji.como.board.dto;

import com.dongjji.como.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateBoardDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String title;
        private String description;
        private boolean isPublic;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String title;
        private String description;
        private boolean isPublic;
        private String author;

        public static Response fromEntity(Board board) {
            return Response.builder()
                    .title(board.getTitle())
                    .description(board.getDescription())
                    .isPublic(board.isPublic())
                    .author(board.getAuthor()).build();
        }
    }
}
