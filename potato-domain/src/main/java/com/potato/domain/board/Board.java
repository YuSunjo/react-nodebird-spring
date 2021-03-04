package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String content;

    private Long memberId;

    private Long retweetId;

    @OneToMany(mappedBy = "board")
    private List<Image> images = new ArrayList<>();

}
