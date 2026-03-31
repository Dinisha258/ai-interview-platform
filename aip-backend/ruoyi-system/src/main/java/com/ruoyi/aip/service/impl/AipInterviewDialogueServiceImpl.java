package com.ruoyi.aip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aip.mapper.AipInterviewDialogueMapper;
import com.ruoyi.aip.domain.AipInterviewDialogue;
import com.ruoyi.aip.service.IAipInterviewDialogueService;

/**
 * 问答对话明细Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
@Service
public class AipInterviewDialogueServiceImpl implements IAipInterviewDialogueService 
{
    @Autowired
    private AipInterviewDialogueMapper aipInterviewDialogueMapper;

    /**
     * 查询问答对话明细
     * 
     * @param id 问答对话明细主键
     * @return 问答对话明细
     */
    @Override
    public AipInterviewDialogue selectAipInterviewDialogueById(Long id)
    {
        return aipInterviewDialogueMapper.selectAipInterviewDialogueById(id);
    }

    /**
     * 查询问答对话明细列表
     * 
     * @param aipInterviewDialogue 问答对话明细
     * @return 问答对话明细
     */
    @Override
    public List<AipInterviewDialogue> selectAipInterviewDialogueList(AipInterviewDialogue aipInterviewDialogue)
    {
        return aipInterviewDialogueMapper.selectAipInterviewDialogueList(aipInterviewDialogue);
    }

    /**
     * 新增问答对话明细
     * 
     * @param aipInterviewDialogue 问答对话明细
     * @return 结果
     */
    @Override
    public int insertAipInterviewDialogue(AipInterviewDialogue aipInterviewDialogue)
    {
        aipInterviewDialogue.setCreateTime(DateUtils.getNowDate());
        return aipInterviewDialogueMapper.insertAipInterviewDialogue(aipInterviewDialogue);
    }

    /**
     * 修改问答对话明细
     * 
     * @param aipInterviewDialogue 问答对话明细
     * @return 结果
     */
    @Override
    public int updateAipInterviewDialogue(AipInterviewDialogue aipInterviewDialogue)
    {
        return aipInterviewDialogueMapper.updateAipInterviewDialogue(aipInterviewDialogue);
    }

    /**
     * 批量删除问答对话明细
     * 
     * @param ids 需要删除的问答对话明细主键
     * @return 结果
     */
    @Override
    public int deleteAipInterviewDialogueByIds(Long[] ids)
    {
        return aipInterviewDialogueMapper.deleteAipInterviewDialogueByIds(ids);
    }

    /**
     * 删除问答对话明细信息
     * 
     * @param id 问答对话明细主键
     * @return 结果
     */
    @Override
    public int deleteAipInterviewDialogueById(Long id)
    {
        return aipInterviewDialogueMapper.deleteAipInterviewDialogueById(id);
    }
}
