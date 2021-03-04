package com.potato.domain.boardHashtag;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardHashtag extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private Long hashtagId;

    private Long boardId;

}
