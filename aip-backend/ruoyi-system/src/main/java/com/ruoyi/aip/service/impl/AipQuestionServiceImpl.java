package com.ruoyi.aip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aip.mapper.AipQuestionMapper;
import com.ruoyi.aip.domain.AipQuestion;
import com.ruoyi.aip.service.IAipQuestionService;

/**
 * 面试题目库Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
@Service
public class AipQuestionServiceImpl implements IAipQuestionService 
{
    @Autowired
    private AipQuestionMapper aipQuestionMapper;

    /**
     * 查询面试题目库
     * 
     * @param id 面试题目库主键
     * @return 面试题目库
     */
    @Override
    public AipQuestion selectAipQuestionById(Long id)
    {
        return aipQuestionMapper.selectAipQuestionById(id);
    }

    /**
     * 查询面试题目库列表
     * 
     * @param aipQuestion 面试题目库
     * @return 面试题目库
     */
    @Override
    public List<AipQuestion> selectAipQuestionList(AipQuestion aipQuestion)
    {
        return aipQuestionMapper.selectAipQuestionList(aipQuestion);
    }

    /**
     * 新增面试题目库
     * 
     * @param aipQuestion 面试题目库
     * @return 结果
     */
    @Override
    public int insertAipQuestion(AipQuestion aipQuestion)
    {
        aipQuestion.setCreateTime(DateUtils.getNowDate());
        return aipQuestionMapper.insertAipQuestion(aipQuestion);
    }

    /**
     * 修改面试题目库
     * 
     * @param aipQuestion 面试题目库
     * @return 结果
     */
    @Override
    public int updateAipQuestion(AipQuestion aipQuestion)
    {
        aipQuestion.setUpdateTime(DateUtils.getNowDate());
        return aipQuestionMapper.updateAipQuestion(aipQuestion);
    }

    /**
     * 批量删除面试题目库
     * 
     * @param ids 需要删除的面试题目库主键
     * @return 结果
     */
    @Override
    public int deleteAipQuestionByIds(Long[] ids)
    {
        return aipQuestionMapper.deleteAipQuestionByIds(ids);
    }

    /**
     * 删除面试题目库信息
     * 
     * @param id 面试题目库主键
     * @return 结果
     */
    @Override
    public int deleteAipQuestionById(Long id)
    {
        return aipQuestionMapper.deleteAipQuestionById(id);
    }
}
