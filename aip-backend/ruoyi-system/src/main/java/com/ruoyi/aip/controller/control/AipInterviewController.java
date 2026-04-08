package com.ruoyi.aip.controller.control;

import java.util.List;

import com.ruoyi.aip.domain.AipInterviewDialogue;
import com.ruoyi.aip.domain.AipInterviewSession;
import com.ruoyi.aip.domain.dto.AgentChatRespDTO;
import com.ruoyi.aip.domain.dto.InterviewChatDTO;
import com.ruoyi.aip.service.IAipInterviewDialogueService;
import com.ruoyi.aip.service.IAipInterviewSessionService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.aip.domain.dto.StartInterviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/control")
public class AipInterviewController extends BaseController {

    @Autowired
    private IAipInterviewSessionService iAipInterviewSessionService;

    @Autowired
    private IAipInterviewDialogueService iAipInterviewDialogueService;

    /**
     * 开始/创建面试会话
     */
    @PostMapping("/start")
    public AjaxResult start(@Validated @RequestBody StartInterviewDTO startDTO) {
        Long userId = SecurityUtils.getUserId();
        Long sessionId = iAipInterviewSessionService.startInterview(userId, startDTO);
        return AjaxResult.success("会话创建成功", sessionId);
    }

    /**
     * 面试核心问答接口：接收学生回答，返回 AI 追问/评价
     */
    @PostMapping("/chat")
    public AjaxResult chat(@Validated @RequestBody InterviewChatDTO chatDTO) {
        AgentChatRespDTO aiResponse = iAipInterviewSessionService.processChat(chatDTO);
        return AjaxResult.success(aiResponse);
    }

    /**
     * 结束面试会话
     */
    @PostMapping("/end/{sessionId}")
    public AjaxResult end(@PathVariable Long sessionId) {
        iAipInterviewSessionService.endInterview(sessionId);
        return AjaxResult.success("面试已结束");
    }

    /**
     * 获取当前用户的面试会话列表（含岗位名称）
     */
    @GetMapping("/sessions")
    public AjaxResult sessions() {
        Long userId = SecurityUtils.getUserId();
        AipInterviewSession query = new AipInterviewSession();
        query.setUserId(userId);
        List<AipInterviewSession> list = iAipInterviewSessionService.selectAipInterviewSessionList(query);
        return AjaxResult.success(list);
    }

    /**
     * 获取指定会话的对话记录
     */
    @GetMapping("/dialogues/{sessionId}")
    public AjaxResult dialogues(@PathVariable Long sessionId) {
        AipInterviewDialogue query = new AipInterviewDialogue();
        query.setSessionId(sessionId);
        List<AipInterviewDialogue> list = iAipInterviewDialogueService.selectAipInterviewDialogueList(query);
        return AjaxResult.success(list);
    }
}
