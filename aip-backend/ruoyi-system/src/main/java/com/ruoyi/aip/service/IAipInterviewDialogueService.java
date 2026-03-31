package com.ruoyi.aip.service;

import java.util.List;
import com.ruoyi.aip.domain.AipInterviewDialogue;

/**
 * 问答对话明细Service接口
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public interface IAipInterviewDialogueService 
{
    /**
     * 查询问答对话明细
     * 
     * @param id 问答对话明细主键
     * @return 问答对话明细
     */
    public AipInterviewDialogue selectAipInterviewDialogueById(Long id);

    /**
     * 查询问答对话明细列表
     * 
     * @param aipInterviewDialogue 问答对话明细
     * @return 问答对话明细集合
     */
    public List<AipInterviewDialogue> selectAipInterviewDialogueList(AipInterviewDialogue aipInterviewDialogue);

    /**
     * 新增问答对话明细
     * 
     * @param aipInterviewDialogue 问答对话明细
     * @return 结果
     */
    public int insertAipInterviewDialogue(AipInterviewDialogue aipInterviewDialogue);

    /**
     * 修改问答对话明细
     * 
     * @param aipInterviewDialogue 问答对话明细
     * @return 结果
     */
    public int updateAipInterviewDialogue(AipInterviewDialogue aipInterviewDialogue);

    /**
     * 批量删除问答对话明细
     * 
     * @param ids 需要删除的问答对话明细主键集合
     * @return 结果
     */
    public int deleteAipInterviewDialogueByIds(Long[] ids);

    /**
     * 删除问答对话明细信息
     * 
     * @param id 问答对话明细主键
     * @return 结果
     */
    public int deleteAipInterviewDialogueById(Long id);
}
