package com.ruoyi.aip.service.impl;

import java.util.List;

import com.ruoyi.aip.domain.dto.StartInterviewDTO;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aip.mapper.AipInterviewSessionMapper;
import com.ruoyi.aip.domain.AipInterviewSession;
import com.ruoyi.aip.service.IAipInterviewSessionService;

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
        // 1. 并发检查：查询该用户是否有状态为“进行中(0)”的会话
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
}
