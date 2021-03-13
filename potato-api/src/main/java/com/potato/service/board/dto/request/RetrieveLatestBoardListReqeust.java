package com.potato.service.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
@ToString
public class RetrieveLatestBoardListReqeust {

    @Min(0)
    private long lastBoardId;

    @Min(1)
    private int size;

}
