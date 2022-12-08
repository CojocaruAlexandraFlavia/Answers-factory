package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.VoteResponseRequest;
import org.jetbrains.annotations.NotNull;

public class VoteResponseRequestMock {

    public static @NotNull VoteResponseRequest voteResponseRequest() {
        VoteResponseRequest voteResponseRequest = new VoteResponseRequest();
        voteResponseRequest.setUserId(1L);
        voteResponseRequest.setResponseId(1L);
        voteResponseRequest.setOption(1);
        return voteResponseRequest;
    }

}
