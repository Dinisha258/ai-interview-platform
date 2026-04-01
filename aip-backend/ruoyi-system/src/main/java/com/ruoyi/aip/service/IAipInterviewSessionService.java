package com.ruoyi.aip.service;

import java.util.List;
import com.ruoyi.aip.domain.AipInterviewSession;
import com.ruoyi.aip.domain.dto.StartInterviewDTO;

/**
 * 面试会话流水Service接口
 *
 * @author ruoyi
 * @date 2026-03-31
 */
public interface IAipInterviewSessionService
{
    /**
     * 查询面试会话流水
     *
     * @param id 面试会话流水主键
     * @return 面试会话流水
     */
    public AipInterviewSession selectAipInterviewSessionById(Long id);

    /**
     * 查询面试会话流水列表
     *
     * @param aipInterviewSession 面试会话流水
     * @return 面试会话流水集合
     */
    public List<AipInterviewSession> selectAipInterviewSessionList(AipInterviewSession aipInterviewSession);

    /**
     * 新增面试会话流水
     *
     * @param aipInterviewSession 面试会话流水
     * @return 结果
     */
    public int insertAipInterviewSession(AipInterviewSession aipInterviewSession);

    /**
     * 修改面试会话流水
     *
     * @param aipInterviewSession 面试会话流水
     * @return 结果
     */
    public int updateAipInterviewSession(AipInterviewSession aipInterviewSession);

    /**
     * 批量删除面试会话流水
     *
     * @param ids 需要删除的面试会话流水主键集合
     * @return 结果
     */
    public int deleteAipInterviewSessionByIds(Long[] ids);

    /**
     * 删除面试会话流水信息
     *
     * @param id 面试会话流水主键
     * @return 结果
     */
    public int deleteAipInterviewSessionById(Long id);

    /**
     * 开启面试会话
     * * @param userId 学生ID
     * @param startDTO 请求参数
     * @return 会话主键ID
     */
    public Long startInterview(Long userId, StartInterviewDTO startDTO);
}
