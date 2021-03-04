package com.potato.service.board.dto.response;

import com.potato.domain.board.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardInfoResponse {

    private final Long id;

    private final String content;

    public static BoardInfoResponse of(Board board) {
        return new BoardInfoResponse(board.getId(), board.getContent());
    }
}
