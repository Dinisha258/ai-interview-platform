package com.ruoyi.aip.controller.control;

import com.ruoyi.aip.domain.dto.InterviewChatDTO;
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

    /**
     * 开始/创建面试会话
     */
    @PostMapping("/start")
    public AjaxResult start(@Validated @RequestBody StartInterviewDTO startDTO) {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();

        // 开启会话
        Long sessionId = iAipInterviewSessionService.startInterview(userId, startDTO);

        return AjaxResult.success("会话创建成功", sessionId);
    }

    /**
     * 面试核心问答接口：接收学生回答，返回 AI 追问/评价
     */
    @PostMapping("/chat")
    public AjaxResult chat(@Validated @RequestBody InterviewChatDTO chatDTO) {
        // TODO
        // 1. 调用 AipDialogueServiceImpl (我们自己建的包装类，包含Redis操作)
        // 2. 发送 HTTP 请求给 FastAPI Agent
        // 3. 拿到 AI 结果后，调用你刚才生成的 aipInterviewDialogueService.insertAipInterviewDialogue() 异步落库

        // 假设 service 层返回的是处理好的 AI 回复对象
//        Object aiResponse = iAipInterviewSessionService.processChat(chatDTO);

//        return AjaxResult.success(aiResponse);
        return null;
    }
}
