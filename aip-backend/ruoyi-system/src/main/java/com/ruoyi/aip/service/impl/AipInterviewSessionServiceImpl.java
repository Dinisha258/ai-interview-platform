package com.ruoyi.aip.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.ruoyi.aip.domain.AipInterviewDialogue;
import com.ruoyi.aip.domain.AipInterviewReport;
import com.ruoyi.aip.domain.AipPosition;
import com.ruoyi.aip.domain.dto.AgentChatReqDTO;
import com.ruoyi.aip.domain.dto.AgentChatRespDTO;
import com.ruoyi.aip.domain.dto.AgentReportReqDTO;
import com.ruoyi.aip.domain.dto.AgentReportRespDTO;
import com.ruoyi.aip.domain.dto.InterviewChatDTO;
import com.ruoyi.aip.domain.dto.StartInterviewDTO;
import com.ruoyi.aip.mapper.AipInterviewDialogueMapper;
import com.ruoyi.aip.mapper.AipInterviewReportMapper;
import com.ruoyi.aip.mapper.AipPositionMapper;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import com.ruoyi.aip.mapper.AipInterviewSessionMapper;
import com.ruoyi.aip.domain.AipInterviewSession;
import com.ruoyi.aip.service.IAipInterviewSessionService;
import org.springframework.web.client.RestTemplate;

/**
 * 面试会话流水Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@Service
public class AipInterviewSessionServiceImpl implements IAipInterviewSessionService
{
    private static final Logger log = LoggerFactory.getLogger(AipInterviewSessionServiceImpl.class);

    @Autowired
    private AipInterviewSessionMapper aipInterviewSessionMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AipInterviewDialogueMapper dialogueMapper;

    @Autowired
    private AipPositionMapper positionMapper;

    @Autowired
    private AipInterviewReportMapper reportMapper;

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${aip.agent.url}")
    private String agentUrl;

    @Value("${aip.redis.ctx-prefix}")
    private String redisCtxPrefix;

    /**
     * 查询面试会话流水
     *
     * @param id 面试会话流水主键
     * @return 面试会话流水
     */
    @Override
    public AipInterviewSession selectAipInterviewSessionById(Long id)
    {
        return aipInterviewSessionMapper.selectAipInterviewSessionById(id);
    }

    /**
     * 查询面试会话流水列表
     *
     * @param aipInterviewSession 面试会话流水
     * @return 面试会话流水
     */
    @Override
    public List<AipInterviewSession> selectAipInterviewSessionList(AipInterviewSession aipInterviewSession)
    {
        return aipInterviewSessionMapper.selectAipInterviewSessionList(aipInterviewSession);
    }

    /**
     * 新增面试会话流水
     *
     * @param aipInterviewSession 面试会话流水
     * @return 结果
     */
    @Override
    public int insertAipInterviewSession(AipInterviewSession aipInterviewSession)
    {
        aipInterviewSession.setCreateTime(DateUtils.getNowDate());
        return aipInterviewSessionMapper.insertAipInterviewSession(aipInterviewSession);
    }

    /**
     * 修改面试会话流水
     *
     * @param aipInterviewSession 面试会话流水
     * @return 结果
     */
    @Override
    public int updateAipInterviewSession(AipInterviewSession aipInterviewSession)
    {
        aipInterviewSession.setUpdateTime(DateUtils.getNowDate());
        return aipInterviewSessionMapper.updateAipInterviewSession(aipInterviewSession);
    }

    /**
     * 批量删除面试会话流水
     *
     * @param ids 需要删除的面试会话流水主键
     * @return 结果
     */
    @Override
    public int deleteAipInterviewSessionByIds(Long[] ids)
    {
        return aipInterviewSessionMapper.deleteAipInterviewSessionByIds(ids);
    }

    /**
     * 删除面试会话流水信息
     *
     * @param id 面试会话流水主键
     * @return 结果
     */
    @Override
    public int deleteAipInterviewSessionById(Long id)
    {
        return aipInterviewSessionMapper.deleteAipInterviewSessionById(id);
    }

    /**
     * 开启面试会话
     */
    @Override
    public Long startInterview(Long userId, StartInterviewDTO startDTO)
    {
        // 1. 并发检查：查询该用户是否有状态为进行中(0)的会话
        AipInterviewSession activeSession = aipInterviewSessionMapper.selectActiveSessionByUserId(userId);

        // 如果有未完成的会话，直接返回其ID，前端拿到后可以恢复进度
        if (activeSession != null) {
            return activeSession.getId();
        }

        // 2. 创建新会话
        AipInterviewSession newSession = new AipInterviewSession();
        newSession.setUserId(userId);
        newSession.setPositionId(startDTO.getPositionId());
        newSession.setTargetLevel(startDTO.getTargetLevel());
        newSession.setStatus(0); // 0-进行中
        newSession.setStartTime(DateUtils.getNowDate());
        newSession.setCreateBy(SecurityUtils.getUsername());
        newSession.setCreateTime(DateUtils.getNowDate());

        // 3. 落库
        aipInterviewSessionMapper.insertAipInterviewSession(newSession);

        // 4. 返回主键 ID
        return newSession.getId();
    }


    /**
     * 核心问答调度逻辑
     */
    @Override
    public AgentChatRespDTO processChat(InterviewChatDTO chatDTO) {
        Long sessionId = chatDTO.getSessionId();

        // 1. 查出会话元数据
        AipInterviewSession session = aipInterviewSessionMapper.selectAipInterviewSessionById(sessionId);
        if (session == null || session.getStatus() != 0) {
            throw new ServiceException("会话不存在或已结束");
        }

        // 查岗位名
        String positionName = positionMapper.selectAipPositionById(session.getPositionId()).getName();

        // 2. 更新 Redis，记录学生这轮的话
        String cacheKey = redisCtxPrefix + sessionId;
        List<AgentChatReqDTO.ChatMessageDTO> historyList = redisCache.getCacheList(cacheKey);
        if (historyList == null) {
            historyList = new java.util.ArrayList<>();
        }

        // 文本优先；纯语音时由 Agent 端做 ASR，此处记录占位
        String userContent = chatDTO.getTextContent();
        if (StringUtils.isEmpty(userContent) && StringUtils.isNotEmpty(chatDTO.getAudioUrl())) {
            userContent = "(语音输入)";
        }
        historyList.add(new AgentChatReqDTO.ChatMessageDTO("user", userContent));

        // 3. 组装给 FastAPI 的请求包
        AgentChatReqDTO agentReq = new AgentChatReqDTO();
        agentReq.setSessionId(sessionId);
        agentReq.setCurrentInput(chatDTO.getTextContent());
        agentReq.setAudioUrl(chatDTO.getAudioUrl());
        agentReq.setPositionName(positionName);
        agentReq.setTargetLevel(session.getTargetLevel());
        // 传给大模型最近的 8 条记录作为记忆
        agentReq.setHistory(historyList.size() > 8 ? historyList.subList(historyList.size() - 8, historyList.size()) : historyList);

        // 4. 调用 Python 微服务
        RestTemplate restTemplate = new RestTemplate();
        AgentChatRespDTO agentResp;
        try {
            ResponseEntity<AgentChatRespDTO> response = restTemplate.postForEntity(agentUrl, agentReq, AgentChatRespDTO.class);
            agentResp = response.getBody();
        } catch (Exception e) {
            // 如果 Python 挂了，移除刚才加到 redis 里的最后一条，防止脏数据
            historyList.remove(historyList.size() - 1);
            redisCache.deleteObject(cacheKey);
            if (!historyList.isEmpty()) {
                redisCache.setCacheList(cacheKey, historyList);
            }
            throw new ServiceException("AI 大脑思考异常，请稍后再试：" + e.getMessage());
        }

        // 5. 将 AI 的回复追加进 Redis，完成本轮闭环
        historyList.add(new AgentChatReqDTO.ChatMessageDTO("ai", agentResp.getAiReply()));
        redisCache.deleteObject(cacheKey);
        redisCache.setCacheList(cacheKey, historyList);
        redisCache.expire(cacheKey, 2, java.util.concurrent.TimeUnit.HOURS);

        // 6. 异步存入 MySQL 落库
        final String finalUserContent = userContent;

        threadPoolTaskExecutor.execute(() -> {
            AipInterviewDialogue dialogueLog = new AipInterviewDialogue();
            dialogueLog.setSessionId(sessionId);
            dialogueLog.setRoundNum(chatDTO.getRoundNum());
            dialogueLog.setUserContent(finalUserContent);
            dialogueLog.setAudioUrl(chatDTO.getAudioUrl());
            dialogueLog.setAiContent(agentResp.getAiReply());
            dialogueLog.setTurnScore(new java.math.BigDecimal(agentResp.getTurnScore() != null ? agentResp.getTurnScore() : 0));
            dialogueLog.setCreateTime(DateUtils.getNowDate());

            dialogueMapper.insertAipInterviewDialogue(dialogueLog);
        });

        // 7. 返回给前端展示
        return agentResp;
    }

    /**
     * 结束面试会话:置状态为已完成,并同步生成评估报告
     */
    @Override
    public void endInterview(Long sessionId) {
        AipInterviewSession session = aipInterviewSessionMapper.selectAipInterviewSessionById(sessionId);
        if (session == null) {
            throw new ServiceException("会话不存在");
        }
        if (session.getStatus() != 0) {
            throw new ServiceException("该会话已结束");
        }
        AipInterviewSession update = new AipInterviewSession();
        update.setId(sessionId);
        update.setStatus(1);
        update.setEndTime(DateUtils.getNowDate());
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(DateUtils.getNowDate());
        aipInterviewSessionMapper.updateAipInterviewSession(update);

        // 面试已结束,尝试生成评估报告(失败只记日志,不影响面试结束本身)
        try {
            generateReport(sessionId, session);
        } catch (Exception e) {
            log.warn("面试会话{}报告生成失败: {}", sessionId, e.getMessage());
        }
    }

    /**
     * 生成面试评估报告:
     * 1. 查整场对话记录 → 2. 调 Agent 报告接口 → 3. 报告落库 + 回填会话总分
     */
    private void generateReport(Long sessionId, AipInterviewSession session) {
        // 1. 查整场对话记录(按轮次排序)
        AipInterviewDialogue query = new AipInterviewDialogue();
        query.setSessionId(sessionId);
        List<AipInterviewDialogue> dialogues = dialogueMapper.selectAipInterviewDialogueList(query);
        if (dialogues == null || dialogues.isEmpty()) {
            log.info("面试会话{}无对话记录,跳过报告生成", sessionId);
            return;
        }

        // 查岗位名(岗位可能已被删除,做个兜底)
        AipPosition position = positionMapper.selectAipPositionById(session.getPositionId());
        String positionName = position != null ? position.getName() : "未知岗位";

        // 2. 组装对话消息列表:一行对话记录 = 一条user回答 + 一条ai提问
        List<AgentReportReqDTO.ChatMessageDTO> messages = new ArrayList<>();
        dialogues.stream()
                .sorted(Comparator.comparing(AipInterviewDialogue::getCreateTime))
                .forEach(d -> {
                    if (StringUtils.isNotEmpty(d.getUserContent())) {
                        messages.add(new AgentReportReqDTO.ChatMessageDTO("user", d.getUserContent()));
                    }
                    if (StringUtils.isNotEmpty(d.getAiContent())) {
                        messages.add(new AgentReportReqDTO.ChatMessageDTO("ai", d.getAiContent()));
                    }
                });

        AgentReportReqDTO reportReq = new AgentReportReqDTO();
        reportReq.setSessionId(sessionId);
        reportReq.setPositionName(positionName);
        reportReq.setTargetLevel(session.getTargetLevel());
        reportReq.setDialogues(messages);

        // 3. 调用 Python Agent 的报告接口(配置里的 /chat 换成 /report)
        String reportUrl = agentUrl.replace("/chat", "/report");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AgentReportRespDTO> response = restTemplate.postForEntity(reportUrl, reportReq, AgentReportRespDTO.class);
        AgentReportRespDTO reportResp = response.getBody();
        if (reportResp == null) {
            throw new ServiceException("报告服务无响应");
        }

        // 4. 报告落库
        AipInterviewReport report = new AipInterviewReport();
        report.setSessionId(sessionId);
        report.setRadarDimensions(reportResp.getRadarDimensions());
        report.setHighlights(reportResp.getHighlights());
        report.setWeaknesses(reportResp.getWeaknesses());
        report.setLearningAdvice(reportResp.getLearningAdvice());
        report.setCreateTime(DateUtils.getNowDate());
        reportMapper.insertAipInterviewReport(report);

        // 5. 回填会话总分
        AipInterviewSession scoreUpdate = new AipInterviewSession();
        scoreUpdate.setId(sessionId);
        scoreUpdate.setTotalScore(reportResp.getTotalScore());
        aipInterviewSessionMapper.updateAipInterviewSession(scoreUpdate);

        log.info("面试会话{}评估报告生成成功,总分: {}", sessionId, reportResp.getTotalScore());
    }
}
