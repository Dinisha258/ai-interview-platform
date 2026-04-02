package com.ruoyi.aip.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class AgentChatReqDTO {
    private Long sessionId;
    private String currentInput;     // 学生本次回答（纯文本输入）
    private String audioUrl;         // 录音文件在 MinIO 上的 URL
    private String positionName;     // 岗位名
    private Integer targetLevel;     // 难度等级
    private List<ChatMessageDTO> history; // Redis 历史记录

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageDTO {
        private String role;
        private String content;
    }
}
