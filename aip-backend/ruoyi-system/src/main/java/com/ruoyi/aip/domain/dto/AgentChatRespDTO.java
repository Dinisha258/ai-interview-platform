package com.ruoyi.aip.domain.dto;

import lombok.Data;

@Data
public class AgentChatRespDTO {
    private String aiReply;          // AI 的回复/追问文本
    private Boolean isFinished;      // 面试是否结束
    private Double turnScore;        // AI 对本轮的打分
    private String ttsAudioBase64;   // TTS 合成的 Base64 音频流
}
