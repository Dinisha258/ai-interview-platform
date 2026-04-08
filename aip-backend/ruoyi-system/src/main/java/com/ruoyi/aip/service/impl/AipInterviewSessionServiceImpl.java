package com.ruoyi.aip.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.ruoyi.aip.domain.AipInterviewDialogue;
import com.ruoyi.aip.domain.dto.AgentChatReqDTO;
import com.ruoyi.aip.domain.dto.AgentChatRespDTO;
import com.ruoyi.aip.domain.dto.InterviewChatDTO;
import com.ruoyi.aip.domain.dto.StartInterviewDTO;
import com.ruoyi.aip.mapper.AipInterviewDialogueMapper;
import com.ruoyi.aip.mapper.AipPositionMapper;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
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
    @Autowired
    private AipInterviewSessionMapper aipInterviewSessionMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AipInterviewDialogueMapper dialogueMapper;

    @Autowired
    private AipPositionMapper positionMapper;

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
            redisCache.setCacheList(cacheKey, historyList);
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
     * 结束面试会话
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
    }
}
