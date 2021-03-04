package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.Image;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String content;

    @NotNull
    private Long memberId;

    private Long retweetId;

    @OneToMany(mappedBy = "board")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Board(String content, Long memberId, Long retweetId) {
        this.content = content;
        this.memberId = memberId;
        this.retweetId = retweetId;
    }

    public static Board newInstance(String content, Long memberId) {
        return Board.builder()
            .content(content)
            .memberId(memberId)
            .build();
    }

}
