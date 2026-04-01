package com.ruoyi.aip.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InterviewChatDTO {
    @NotNull(message = "会话ID不能为空")
    private Long sessionId;

    @NotNull(message = "当前轮次不能为空")
    private Integer roundNum;

    // 如果是语音，前端传 URL；如果是文本，直接传文字
    private String audioUrl;
    private String textContent;
}
