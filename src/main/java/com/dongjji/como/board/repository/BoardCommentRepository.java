package com.dongjji.como.board.repository;

import com.dongjji.como.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findAllByBoardId(Long boardId);
}
