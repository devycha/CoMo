package com.dongjji.como.board.repository;

import com.dongjji.como.board.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    boolean existsBoardLikeByBoardIdAndUserEmail(Long boardId, String userEmail);
    int countAllByBoardId(Long boardId);
}
