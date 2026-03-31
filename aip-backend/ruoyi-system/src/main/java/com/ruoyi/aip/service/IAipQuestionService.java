package com.ruoyi.aip.service;

import java.util.List;
import com.ruoyi.aip.domain.AipQuestion;

/**
 * 面试题目库Service接口
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public interface IAipQuestionService 
{
    /**
     * 查询面试题目库
     * 
     * @param id 面试题目库主键
     * @return 面试题目库
     */
    public AipQuestion selectAipQuestionById(Long id);

    /**
     * 查询面试题目库列表
     * 
     * @param aipQuestion 面试题目库
     * @return 面试题目库集合
     */
    public List<AipQuestion> selectAipQuestionList(AipQuestion aipQuestion);

    /**
     * 新增面试题目库
     * 
     * @param aipQuestion 面试题目库
     * @return 结果
     */
    public int insertAipQuestion(AipQuestion aipQuestion);

    /**
     * 修改面试题目库
     * 
     * @param aipQuestion 面试题目库
     * @return 结果
     */
    public int updateAipQuestion(AipQuestion aipQuestion);

    /**
     * 批量删除面试题目库
     * 
     * @param ids 需要删除的面试题目库主键集合
     * @return 结果
     */
    public int deleteAipQuestionByIds(Long[] ids);

    /**
     * 删除面试题目库信息
     * 
     * @param id 面试题目库主键
     * @return 结果
     */
    public int deleteAipQuestionById(Long id);
}
