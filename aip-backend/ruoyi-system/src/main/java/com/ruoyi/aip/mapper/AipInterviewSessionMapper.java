package com.ruoyi.aip.mapper;

import java.util.List;
import com.ruoyi.aip.domain.AipInterviewSession;

/**
 * 面试会话流水Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-31
 */
public interface AipInterviewSessionMapper
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
     * 删除面试会话流水
     *
     * @param id 面试会话流水主键
     * @return 结果
     */
    public int deleteAipInterviewSessionById(Long id);

    /**
     * 批量删除面试会话流水
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAipInterviewSessionByIds(Long[] ids);

    /**
     * 查询当前用户进行中（状态为0）的面试会话
     *
     * @param userId 用户ID
     * @return 活跃的面试会话
     */
    public AipInterviewSession selectActiveSessionByUserId(Long userId);
}
