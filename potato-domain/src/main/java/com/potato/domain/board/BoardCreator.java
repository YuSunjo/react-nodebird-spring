package com.potato.domain.board;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreator {

    public static Board create(String content, Long memberId) {
        return Board.builder()
            .content(content)
            .memberId(memberId)
            .build();
    }

}
