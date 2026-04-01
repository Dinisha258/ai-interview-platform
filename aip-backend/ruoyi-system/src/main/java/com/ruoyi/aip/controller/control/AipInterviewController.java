package com.ruoyi.aip.controller.control;

import com.ruoyi.aip.service.IAipInterviewSessionService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.aip.domain.dto.StartInterviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
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
}
